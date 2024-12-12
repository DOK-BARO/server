package kr.kro.dokbaro.server.common.dto.option

/**
 * 목록 조회 시 정렬 옵션을 담당합니다.
 */
data class SortOption<T>(
	val keyword: T,
	val direction: SortDirection = SortDirection.ASC,
)