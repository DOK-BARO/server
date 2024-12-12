package kr.kro.dokbaro.server.common.dto.response

/**
 * pagination 적용 시 응답값을 전송하는 DTO입니다.
 */
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