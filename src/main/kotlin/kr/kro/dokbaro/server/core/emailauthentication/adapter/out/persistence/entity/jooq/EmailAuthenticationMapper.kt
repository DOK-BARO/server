package kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication
import org.jooq.generated.tables.records.EmailAuthenticationRecord

@Mapper
class EmailAuthenticationMapper {
	fun toEmailAuthentication(record: EmailAuthenticationRecord?): EmailAuthentication? =
		record?.let {
			EmailAuthentication(
				address = it.address,
				code = it.code,
				authenticated = it.authenticated,
				used = it.used,
				id = it.id,
			)
		}
}