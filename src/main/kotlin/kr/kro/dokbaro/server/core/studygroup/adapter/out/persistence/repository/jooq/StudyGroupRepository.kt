package kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq.StudyGroupMapper
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JStudyGroup
import org.jooq.generated.tables.JStudyGroupMember
import org.jooq.generated.tables.records.StudyGroupRecord
import org.springframework.stereotype.Repository

@Repository
class StudyGroupRepository(
	private val dslContext: DSLContext,
	private val studyGroupMapper: StudyGroupMapper,
) {
	companion object {
		private val STUDY_GROUP = JStudyGroup.STUDY_GROUP
		private val STUDY_GROUP_MEMBER = JStudyGroupMember.STUDY_GROUP_MEMBER
	}

	fun insert(studyGroup: StudyGroup): Long {
		val studyGroupId: Long =
			dslContext
				.insertInto(
					STUDY_GROUP,
					STUDY_GROUP.NAME,
					STUDY_GROUP.INTRODUCTION,
					STUDY_GROUP.PROFILE_IMAGE_URL,
					STUDY_GROUP.INVITE_CODE,
				).values(
					studyGroup.name,
					studyGroup.introduction?.toByteArray(),
					studyGroup.profileImageUrl,
					studyGroup.inviteCode.value,
				).returningResult(STUDY_GROUP.ID)
				.fetchOneInto(Long::class.java)!!

		val insertQuery =
			studyGroup.studyMembers.map {
				dslContext
					.insertInto(
						STUDY_GROUP_MEMBER,
						STUDY_GROUP_MEMBER.STUDY_GROUP_ID,
						STUDY_GROUP_MEMBER.MEMBER_ID,
						STUDY_GROUP_MEMBER.MEMBER_ROLE,
					).values(
						studyGroupId,
						it.memberId,
						it.role.name,
					)
			}

		dslContext.batch(insertQuery).execute()

		return studyGroupId
	}

	fun findByInviteCode(inviteCode: String): StudyGroup? {
		val fetchGroups: Map<StudyGroupRecord, Result<Record>> =
			dslContext
				.select()
				.from(STUDY_GROUP)
				.join(STUDY_GROUP_MEMBER)
				.on(STUDY_GROUP_MEMBER.STUDY_GROUP_ID.eq(STUDY_GROUP.ID))
				.where(STUDY_GROUP.INVITE_CODE.eq(inviteCode))
				.fetchGroups(STUDY_GROUP)

		return studyGroupMapper.recordToStudyGroup(fetchGroups)
	}

	fun update(studyGroup: StudyGroup) {
		dslContext
			.update(STUDY_GROUP)
			.set(STUDY_GROUP.NAME, studyGroup.name)
			.set(STUDY_GROUP.INTRODUCTION, studyGroup.introduction?.toByteArray())
			.set(STUDY_GROUP.PROFILE_IMAGE_URL, studyGroup.profileImageUrl)
			.set(STUDY_GROUP.INVITE_CODE, studyGroup.inviteCode.value)
			.where(STUDY_GROUP.ID.eq(studyGroup.id))
			.execute()

		dslContext.delete(STUDY_GROUP_MEMBER).where(STUDY_GROUP_MEMBER.STUDY_GROUP_ID.eq(studyGroup.id)).execute()

		val insertQuery =
			studyGroup.studyMembers.map {
				dslContext
					.insertInto(
						STUDY_GROUP_MEMBER,
						STUDY_GROUP_MEMBER.STUDY_GROUP_ID,
						STUDY_GROUP_MEMBER.MEMBER_ID,
						STUDY_GROUP_MEMBER.MEMBER_ROLE,
					).values(
						studyGroup.id,
						it.memberId,
						it.role.name,
					)
			}

		dslContext.batch(insertQuery).execute()
	}
}