package kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.auth.email.domain.EmailCertificatedAccount
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JAccountPassword
import org.jooq.generated.tables.JMemberRole
import org.jooq.generated.tables.records.MemberRecord

@Mapper
class EmailAccountMapper {
	companion object {
		private val ACCOUNT_PASSWORD = JAccountPassword.ACCOUNT_PASSWORD
		private val MEMBER_ROLE = JMemberRole.MEMBER_ROLE
	}

	fun toEmailCertificatedAccount(record: Map<MemberRecord, Result<Record>>): EmailCertificatedAccount? =
		record
			.map {
				EmailCertificatedAccount(
					password = it.value.getValues(ACCOUNT_PASSWORD.PASSWORD).first(),
					certificationId = UUIDUtils.byteArrayToUUID(byteArray = it.key.certificationId),
					role =
						it.value
							.getValues(MEMBER_ROLE.NAME)
							.toSet(),
				)
			}.firstOrNull()
}