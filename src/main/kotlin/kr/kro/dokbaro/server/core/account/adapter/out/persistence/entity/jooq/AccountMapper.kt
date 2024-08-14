package kr.kro.dokbaro.server.core.account.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.account.domain.Account
import kr.kro.dokbaro.server.core.account.domain.Role
import org.jooq.Result
import org.jooq.generated.tables.records.AccountRecord
import org.jooq.generated.tables.records.RoleRecord
import org.springframework.stereotype.Component

@Component
class AccountMapper {
	fun mapTo(record: Map<AccountRecord, Result<RoleRecord>>): Account? =
		record
			.map {
				Account(
					it.key.socialId,
					AuthProvider.valueOf(it.key.provider),
					it.value.map { Role.valueOf(it.name) }.toSet(),
					it.key.createdAt,
					it.key.id,
				)
			}.firstOrNull()
}