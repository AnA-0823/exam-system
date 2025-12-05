package priv.ana.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import priv.ana.core.enums.ResponseStatus;
import priv.ana.core.enums.UserRoleEnum;
import priv.ana.core.utils.JwtUtils;
import priv.ana.core.web.domain.Response;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class AuthFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final ObjectMapper objectMapper;

    public AuthFilterFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return new AuthFilter();
    }

    public class AuthFilter implements GatewayFilter, Ordered {


        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            List<String> authorizationHeader = request.getHeaders().get("Authorization");
            if (authorizationHeader == null || authorizationHeader.isEmpty()) {
                log.warn("请求头为空");
                return setUnauthorized(response);
            }

            try {
                String token = authorizationHeader.get(0).replace("Bearer ", "");

                Jws<Claims> claimsJws = JwtUtils.verify(token);
                String userId = claimsJws.getPayload().get("userId", String.class);
                String role = claimsJws.getPayload().get("role", String.class);
                if (role == null || role.isEmpty() || userId == null || userId.isEmpty()) {
                    log.info("拦截请求 userId:{},role:{}", userId, role);
                    return setUnauthorized(response);
                }

                try {
                    ServerHttpRequest modifiedRequest = request.mutate()
                            .headers(headers -> {
                                headers.remove("Authorization");
                                headers.add("User-Id", userId);
                                headers.add("User-Role", Objects.requireNonNull(UserRoleEnum.getCode(role)).toString());
                            }).build();
                    log.info("放通请求 userId:{},role:{}", userId, role);
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } catch (Exception e) {
                    log.info("拦截请求 userId:{},role:{}", userId, role);
                    return setUnauthorized(response);
                }
            } catch (Exception e) {
                log.warn("jwt解码失败", e);
                return setUnauthorized(response);
            }
        }

        /**
         * 为响应添加unauthorized
         *
         * @param response 响应
         * @return 响应式流
         */
        private Mono<Void> setUnauthorized(ServerHttpResponse response) {
            try {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                String responseBody = objectMapper.writeValueAsString(Response.fail(ResponseStatus.UNAUTHORIZED));
                DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }

        /**
         * 保证最高优先级
         *
         * @return 最高优先级数
         */
        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }
    }
}
