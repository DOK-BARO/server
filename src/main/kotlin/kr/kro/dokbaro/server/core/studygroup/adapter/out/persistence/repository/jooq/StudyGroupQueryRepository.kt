package kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq.StudyGroupMapper
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import org.jooq.DSLContext
import org.jooq.Record5
import org.jooq.Result
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JStudyGroup
import org.jooq.generated.tables.JStudyGroupMember
import org.jooq.generated.tables.records.StudyGroupRecord
import org.jooq.impl.DSL.select
import org.springframework.stereotype.Repository

@Repository
class StudyGroupQueryRepository(
	private val dslContext: DSLContext,
	private val studyGroupMapper: StudyGroupMapper,
) {
	companion object {
		private val STUDY_GROUP = JStudyGroup.STUDY_GROUP
		private val STUDY_GROUP_MEMBER = JStudyGroupMember.STUDY_GROUP_MEMBER
		private val MEMBER = JMember.MEMBER
	}

	fun findAllByStudyMemberId(memberId: Long): Collection<StudyGroupSummary> {
		val record: Result<StudyGroupRecord> =
			dslContext
				.select(
					STUDY_GROUP.ID,
					STUDY_GROUP.NAME,
					STUDY_GROUP.PROFILE_IMAGE_URL,
				).from(STUDY_GROUP)
				.where(
					STUDY_GROUP.ID.`in`(
						select(
							STUDY_GROUP_MEMBER.STUDY_GROUP_ID,
						).from(STUDY_GROUP_MEMBER)
							.where(STUDY_GROUP_MEMBER.MEMBER_ID.eq(memberId)),
					),
				).fetchInto(STUDY_GROUP)

		return studyGroupMapper.toStudyGroupSummary(record)
	}

	fun findAllStudyGroupMembers(id: Long): Collection<StudyGroupMemberResult> {
		val record: Result<Record5<Long, Long, Long, String, String>> =
			dslContext
				.select(
					STUDY_GROUP_MEMBER.ID,
					STUDY_GROUP_MEMBER.MEMBER_ID,
					STUDY_GROUP_MEMBER.STUDY_GROUP_ID,
					MEMBER.NICKNAME,
					STUDY_GROUP_MEMBER.MEMBER_ROLE,
				).from(STUDY_GROUP_MEMBER)
				.join(MEMBER)
				.on(MEMBER.ID.eq(STUDY_GROUP_MEMBER.MEMBER_ID))
				.where(STUDY_GROUP_MEMBER.STUDY_GROUP_ID.eq(id))
				.fetch()

		return studyGroupMapper.toStudyGroupMemberResult(record)
	}
}