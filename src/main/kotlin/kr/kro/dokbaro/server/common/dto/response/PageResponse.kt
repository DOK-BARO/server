package kr.kro.dokbaro.server.common.dto.response

/**
 * pagination 적용 시 응답값을 전송하는 DTO입니다.
 */
data class PageResponse<T>(
	val endPageNumber: Long,
	val data: Collection<T>,
) {
	companion object {
		/**
		 * 총 요소 개수 및 페이지 크기를 통해 마지막 페이지 번호를 계산압니다.
		 */
		fun <T> of(
			totalElementCount: Long,
			pageSize: Long,
			data: Collection<T>,
		): PageResponse<T> =
			PageResponse(
				endPageNumber = (totalElementCount - 1) / pageSize + 1,
				data = data,
			)
	}
}