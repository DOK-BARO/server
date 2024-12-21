package kr.kro.dokbaro.server.fixture.adapter.input.web

import kr.kro.dokbaro.server.common.dto.option.SortDirection
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName

inline fun <reified T : Enum<T>> pageRequestParams(
	page: Int = 1,
	size: Int = 10,
	sort: T = enumValues<T>().first(),
	direction: SortDirection = SortDirection.ASC,
	etc: Map<String, String> = emptyMap(),
) = mapOf(
	"page" to page.toString(),
	"size" to size.toString(),
	"sort" to sort.name,
	"direction" to direction.name,
) + etc

inline fun <reified T : Enum<T>> pageQueryParameters(): Array<ParameterDescriptor> =
	arrayOf(
		parameterWithName("page").description("결과 페이지 번호. 1부터 시작."),
		parameterWithName("size").description("페이지당 결과 수."),
		parameterWithName("sort").description("정렬 기준. [${enumValues<T>().joinToString(", ") { it.name }}]"),
		parameterWithName("direction").optional().description("정렬 방향. 가능한 값은 'ASC' 또는 'DESC'"),
	)

fun endPageNumberFields(): FieldDescriptor =
	fieldWithPath("endPageNumber").type(JsonFieldType.NUMBER).description("마지막 페이지 번호.")