package gr.project.dualeasyauth.config


import gr.project.dualeasyauth.service.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userService: CustomOAuth2UserService,
    private val successHandler: OAuth2LoginSuccessHandler,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login {
                it.userInfoEndpoint { userInfo -> userInfo.userService(userService) }
                it.successHandler(successHandler)
            }
            .cors { }
            .csrf { it.disable() }
            .headers { it.frameOptions { frameOptions -> frameOptions.disable() } }

        return http.build()
    }
}







