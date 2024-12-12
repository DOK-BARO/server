package kr.kro.dokbaro.server.common.dto.option

/**
 * 목록 조회 시 pagination을 관리합니다.
 * 첫 페이지 번호를 1로 지정힙니다.
 * 페이지와 size 값을 db query에 사용할 수 있도록 offset과 limit 형태로 변환합니다.
 */
data class PageOption(
	val offset: Long,
	val limit: Long,
) {
	companion object {
		fun of(
			page: Long,
			size: Long,
		) = PageOption(
			offset = (page - 1) * size,
			limit = size,
		)
	}
}