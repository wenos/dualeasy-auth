package gr.project.dualeasyauth.data.repository

import gr.project.dualeasyauth.data.model.User
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}
