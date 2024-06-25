package kr.kro.dokbaro.server.domain.account.adapter.output.jooq

import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.model.Provider
import kr.kro.dokbaro.server.domain.account.model.Role
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
					it.key.id,
					it.key.socialId,
					Provider.valueOf(it.key.provider),
					it.value.map { Role.valueOf(it.name) }.toSet(),
					it.key.createdAt,
				)
			}.firstOrNull()
}