package kr.kro.dokbaro.server.common.dto.option

/**
 * 목록 조회 시 pagination을 관리합니다.
 * 첫 페이지 번호를 1로 지정힙니다.
 * 페이지와 size 값을 db query에 사용할 수 있도록 offset과 limit 형태로 변환합니다.
 */
const val DEFAULT_PAGE_NUMBER = 1L
const val DEFAULT_PAGE_SIZE = 100L

data class PageOption<T : Enum<T>>(
	val offset: Long,
	val limit: Long,
	val size: Long,
	val sort: T,
	val direction: SortDirection,
) {
	companion object {
		inline fun <reified T : Enum<T>> of(
			page: Long = DEFAULT_PAGE_NUMBER,
			size: Long = DEFAULT_PAGE_SIZE,
			sort: T = enumValues<T>().first(),
			direction: SortDirection = SortDirection.ASC,
		): PageOption<T> {
			require(page > 0) { "페이지 번호는 1보다 커야 합니다." }
			require(size > 0) { "페이지 크기는 0보다 커야 합니다." }

			return PageOption(
				offset = (page - 1) * size,
				limit = size,
				size = size,
				sort = sort,
				direction = direction,
			)
		}
	}
}