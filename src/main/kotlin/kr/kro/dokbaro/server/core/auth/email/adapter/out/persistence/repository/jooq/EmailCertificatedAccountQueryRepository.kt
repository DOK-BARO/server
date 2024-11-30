package kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence.entity.jooq.EmailAccountMapper
import kr.kro.dokbaro.server.core.auth.email.domain.EmailCertificatedAccount
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JAccountPassword
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JMemberRole
import org.jooq.generated.tables.records.MemberRecord
import org.springframework.stereotype.Repository

@Repository
class EmailCertificatedAccountQueryRepository(
	private val dslContext: DSLContext,
	private val emailAccountMapper: EmailAccountMapper,
) {
	companion object {
		private val ACCOUNT_PASSWORD = JAccountPassword.ACCOUNT_PASSWORD
		private val MEMBER = JMember.MEMBER
		private val MEMBER_ROLE = JMemberRole.MEMBER_ROLE
	}

	fun findByEmail(email: String): EmailCertificatedAccount? {
		val record: Map<MemberRecord, Result<Record>> =
			dslContext
				.select()
				.from(MEMBER)
				.join(ACCOUNT_PASSWORD)
				.on(ACCOUNT_PASSWORD.MEMBER_ID.eq(MEMBER.ID))
				.join(MEMBER_ROLE)
				.on(MEMBER_ROLE.MEMBER_ID.eq(MEMBER.ID))
				.where(MEMBER.EMAIL.eq(email))
				.fetchGroups(MEMBER)

		return emailAccountMapper.toEmailCertificatedAccount(record)
	}
}