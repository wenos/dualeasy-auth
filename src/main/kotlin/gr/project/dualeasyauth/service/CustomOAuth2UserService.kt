package gr.project.dualeasyauth.service
import gr.project.dualeasyauth.data.model.User
import gr.project.dualeasyauth.data.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val email = oAuth2User.attributes["default_email"] as String
        val user = userRepository.findByEmail(email) ?: userRepository.save(User(email = email, role = "USER"))

        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role}"))
        return DefaultOAuth2User(authorities, oAuth2User.attributes, "default_email")
    }
}
