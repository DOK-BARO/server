package kr.kro.dokbaro.server.common.dto.response

data class PageResponse<T>(
	val endPageNumber: Long,
	val data: Collection<T>,
) {
	companion object {
		fun <T> of(
			totalElementCount: Long,
			pageSize: Long,
			data: Collection<T>,
		): PageResponse<T> =
			PageResponse(
				endPageNumber = totalElementCount / pageSize + 1,
				data = data,
			)
	}
}