package kr.kro.dokbaro.server.core.image.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.image.application.port.input.FindImageUseCase
import kr.kro.dokbaro.server.core.image.application.port.input.UploadImageUseCase
import kr.kro.dokbaro.server.core.image.domain.ImageTarget
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ImageController::class)
class ImageControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var uploadImageUseCase: UploadImageUseCase

	@MockkBean
	lateinit var findImageUseCase: FindImageUseCase

	init {
		"이미지 저장을 수행한다" {
			every { uploadImageUseCase.upload(any(), any()) } returns
				"https://dev.dokbaro.kro.kr/images/images/dev.png"

			performWithMultipart(
				path = Path("/images/{target}", ImageTarget.MEMBER_PROFILE.name),
			).andExpect(status().isOk)
				.andDo(
					print(
						"image/upload",
						pathParameters(
							parameterWithName("target").description("이미지 저장 대상 (이하 상세)"),
						),
						responseFields(
							fieldWithPath("url")
								.type(JsonFieldType.STRING)
								.description("저장된 이미지의 경로"),
						),
					),
				)
		}
	}
}