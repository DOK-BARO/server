package kr.kro.dokbaro.server.domain.auth.adapter.output.github.external.provideraccount

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Plan(
	val name: String?,
	val space: Long?,
	val collaborators: Int?,
	val privateRepos: Int?,
)