package kr.kro.dokbaro.server.common.dto.option

data class SortOption<T>(
	val keyword: T,
	val direction: SortDirection = SortDirection.ASC,
)