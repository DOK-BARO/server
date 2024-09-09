package kr.kro.dokbaro.server.core.studygroup.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StudyGroupTest :
	StringSpec({

		"study group을 생성한 사람은 leader로 초기설정된다" {
			val studyGroup =
				StudyGroup(
					"name",
					"introduction",
					"profileImage.png",
					4,
				)

			studyGroup.studyMembers.count() shouldBe 1
			studyGroup.studyMembers.first().role shouldBe StudyMemberRole.LEADER
		}
	})