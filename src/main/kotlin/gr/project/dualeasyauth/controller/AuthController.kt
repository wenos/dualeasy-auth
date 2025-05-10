package gr.project.dualeasyauth.controller

import gr.project.dualeasyauth.data.repository.UserRepository
import gr.project.dualeasyauth.service.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) {
    @GetMapping("/token")
    fun getToken(authentication: Authentication): ResponseEntity<Map<String, String>> {
        val user = userRepository.findByEmail(authentication.name) ?: return ResponseEntity.status(401).build()
        val token = jwtService.generateToken(user)
        return ResponseEntity.ok(mapOf("token" to token))
    }

    @GetMapping("/success")
    fun successPage(authentication: Authentication): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("message" to "Успешная авторизация", "user" to authentication.name))
    }

    @GetMapping("/validate")
    fun validateToken(@RequestParam token: String): ResponseEntity<AuthInfoResponse> {
        val claims = jwtService.validateToken(token) ?: return ResponseEntity.status(401).build()
        val email = claims.subject
        val user = userRepository.findByEmail(email) ?: return ResponseEntity.status(404).build()
        return ResponseEntity.ok(AuthInfoResponse(user.id.toString(), user.role))
    }
}

data class AuthInfoResponse(
    val clientId: String,
    val role: String,
)