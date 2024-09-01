package kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication
import org.jooq.DSLContext
import org.jooq.generated.tables.JEmailAuthentication
import org.springframework.stereotype.Repository

@Repository
class EmailAuthenticationJooqRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val EMAIL_AUTHENTICATION = JEmailAuthentication.EMAIL_AUTHENTICATION
	}

	fun save(emailAuthentication: EmailAuthentication) {
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
			).execute()
	}

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