package kr.kro.dokbaro.server.core.studygroup.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupJooqRepository
import kr.kro.dokbaro.server.core.studygroup.application.port.out.SaveStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup

@PersistenceAdapter
class StudyGroupPersistenceAdapter(
	private val studyGroupRepository: StudyGroupJooqRepository,
) : SaveStudyGroupPort {
	override fun save(studyGroup: StudyGroup): Long = studyGroupRepository.save(studyGroup)
}