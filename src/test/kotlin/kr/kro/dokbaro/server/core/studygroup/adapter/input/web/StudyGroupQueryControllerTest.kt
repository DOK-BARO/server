package kr.kro.dokbaro.server.core.studygroup.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllMyStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(StudyGroupQueryController::class)
class StudyGroupQueryControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var findAllMyStudyGroupUseCase: FindAllMyStudyGroupUseCase

	init {
		"로그인한 사용자의 그룹 참여 목록을 보여준다" {
			every { findAllMyStudyGroupUseCase.findAll(any()) } returns
				listOf(
					StudyGroupSummary("C 스터디", "ccc.png", 1),
					StudyGroupSummary("JAVA 스터디", "ccc.png", 1),
					StudyGroupSummary("모각코 합시다", "ccc.png", 1),
				)

			performGet(Path("/study-groups/my"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"study-group/get-my-study-group-list",
						responseFields(
							fieldWithPath("[].name").type(JsonFieldType.STRING).description("스터디 그룹 명"),
							fieldWithPath("[].profileImageUrl").type(JsonFieldType.STRING).description("스터디 그룹 프로필 사진"),
							fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("스터디 그룹 ID"),
						),
					),
				)
		}
	}
}