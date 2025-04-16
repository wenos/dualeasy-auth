package gr.project.dualeasyauth.config

import gr.project.dualeasyauth.data.repository.UserRepository
import gr.project.dualeasyauth.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.net.URLEncoder

@Component
class OAuth2LoginSuccessHandler(
    private val jwtService: JwtService,
    private val userRepository: UserRepository
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val email = authentication.name
        val user = userRepository.findByEmail(email)

        if (user != null) {
            val token = jwtService.generateToken(user)
            val encodedToken = URLEncoder.encode(token, "UTF-8")
            println(token)
            println(encodedToken)

            response.sendRedirect("http://localhost:3000/after-login?token=$encodedToken")
        } else {
            response.sendRedirect("http://localhost:3000/error")
        }
    }
}