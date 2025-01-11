package kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication
import org.jooq.DSLContext
import org.jooq.generated.tables.JEmailAuthentication.EMAIL_AUTHENTICATION
import org.springframework.stereotype.Repository

@Repository
class EmailAuthenticationRepository(
	private val dslContext: DSLContext,
) {
	fun insert(emailAuthentication: EmailAuthentication): Long =
		dslContext
			.insertInto(
				EMAIL_AUTHENTICATION,
				EMAIL_AUTHENTICATION.ADDRESS,
				EMAIL_AUTHENTICATION.CODE,
				EMAIL_AUTHENTICATION.AUTHENTICATED,
				EMAIL_AUTHENTICATION.USED,
			).values(
				emailAuthentication.address,
				emailAuthentication.code,
				emailAuthentication.authenticated,
				emailAuthentication.used,
			).returningResult(EMAIL_AUTHENTICATION.ID)
			.fetchOneInto(Long::class.java)!!

	fun update(emailAuthentication: EmailAuthentication) {
		dslContext
			.update(EMAIL_AUTHENTICATION)
			.set(EMAIL_AUTHENTICATION.ADDRESS, emailAuthentication.address)
			.set(EMAIL_AUTHENTICATION.CODE, emailAuthentication.code)
			.set(EMAIL_AUTHENTICATION.AUTHENTICATED, emailAuthentication.authenticated)
			.set(EMAIL_AUTHENTICATION.USED, emailAuthentication.used)
			.where(EMAIL_AUTHENTICATION.ID.eq(emailAuthentication.id))
			.execute()
	}
}