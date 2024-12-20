package kr.kro.dokbaro.server.core.studygroup.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllMyStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindStudyGroupDetailUseCase
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import kr.kro.dokbaro.server.core.studygroup.query.sort.MyStudyGroupSortKeyword
import kr.kro.dokbaro.server.fixture.adapter.input.web.endPageNumberFields
import kr.kro.dokbaro.server.fixture.adapter.input.web.pageQueryParameters
import kr.kro.dokbaro.server.fixture.adapter.input.web.pageRequestParams
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(StudyGroupQueryController::class)
class StudyGroupQueryControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var findAllMyStudyGroupUseCase: FindAllMyStudyGroupUseCase

	@MockkBean
	lateinit var findStudyGroupDetailUseCase: FindStudyGroupDetailUseCase

	init {
		"로그인한 사용자의 그룹 참여 목록을 보여준다" {
			every { findAllMyStudyGroupUseCase.findAll(any(), any()) } returns
				PageResponse.of(
					totalElementCount = 1000,
					pageSize = 10,
					listOf(
						StudyGroupSummary(1, "C 스터디", "ccc.png", 5, StudyGroupSummary.Leader(1, "홍길동")),
						StudyGroupSummary(2, "JAVA 스터디", "ccc.png", 5, StudyGroupSummary.Leader(1, "홍길동")),
						StudyGroupSummary(3, "모각코 합시다", "ccc.png", 5, StudyGroupSummary.Leader(1, "홍길동")),
					),
				)

			performGet(Path("/study-groups/my"), pageRequestParams<MyStudyGroupSortKeyword>())
				.andExpect(status().isOk)
				.andDo(
					print(
						"study-group/get-my-study-group-list",
						queryParameters(
							*pageQueryParameters<MyStudyGroupSortKeyword>(),
						),
						responseFields(
							endPageNumberFields(),
							fieldWithPath("data[].name").type(JsonFieldType.STRING).description("스터디 그룹 명"),
							fieldWithPath("data[].profileImageUrl").type(JsonFieldType.STRING).description("스터디 그룹 프로필 사진"),
							fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("스터디 그룹 ID"),
							fieldWithPath("data[].studyMemberCount").description("스터디 그룹 멤버 수").type(JsonFieldType.NUMBER),
							fieldWithPath("data[].leader").optional().description("스터디 그룹 리더 정보"),
							fieldWithPath("data[].leader.id").description("리더 ID").type(JsonFieldType.NUMBER),
							fieldWithPath("data[].leader.nickname").description("리더 닉네임").type(JsonFieldType.STRING),
						),
					),
				)
		}

		"스터디 그룹 정보를 조회한다" {
			every { findStudyGroupDetailUseCase.findStudyGroupDetailBy(any()) } returns
				StudyGroupDetail(
					name = "AI 연구 스터디",
					introduction = "인공지능과 머신러닝에 대해 함께 공부하는 스터디입니다.",
					profileImageUrl = "https://example.com/group-profile.jpg",
					studyMembers =
						listOf(
							StudyGroupDetail.StudyMember(
								id = 1L,
								nickname = "철수",
								profileImageUrl = "https://example.com/member1-profile.jpg",
								role = "LEADER",
							),
							StudyGroupDetail.StudyMember(
								id = 2L,
								nickname = "영희",
								profileImageUrl = "https://example.com/member2-profile.jpg",
								role = "MEMBER",
							),
							StudyGroupDetail.StudyMember(
								id = 3L,
								nickname = "민수",
								profileImageUrl = null,
								role = "MEMBER",
							),
						),
					inviteCode = "ABC123",
					id = 101L,
				)

			performGet(Path("/study-groups/{id}", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"study-group/get-study-group-detail",
						pathParameters(parameterWithName("id").description("study group ID")),
						responseFields(
							fieldWithPath("name").description("스터디 그룹의 이름"),
							fieldWithPath("introduction").description("스터디 그룹 소개").optional(),
							fieldWithPath("profileImageUrl").description("스터디 그룹 프로필 이미지의 URL").optional(),
							fieldWithPath("studyMembers").description("스터디 그룹의 멤버 목록"),
							fieldWithPath("studyMembers[].id").description("멤버의 고유 ID"),
							fieldWithPath("studyMembers[].nickname").description("멤버의 닉네임"),
							fieldWithPath("studyMembers[].profileImageUrl").description("멤버의 프로필 이미지 URL").optional(),
							fieldWithPath("studyMembers[].role").description("스터디 그룹 내 멤버의 역할 (LEADER, MEMBER)"),
							fieldWithPath("inviteCode").description("스터디 그룹에 참여할 수 있는 초대 코드"),
							fieldWithPath("id").description("스터디 그룹의 고유 ID"),
						),
					),
				)
		}
	}
}