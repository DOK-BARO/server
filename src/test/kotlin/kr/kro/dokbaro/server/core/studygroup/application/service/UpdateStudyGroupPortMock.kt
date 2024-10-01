package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.core.studygroup.application.port.out.UpdateStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup

class UpdateStudyGroupPortMock : UpdateStudyGroupPort {
	var storage: StudyGroup? = null

	override fun update(studyGroup: StudyGroup) {
		storage = studyGroup
	}

	fun clear() {
		storage = null
	}
}