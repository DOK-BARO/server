package kr.kro.dokbaro.server.core.member.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberQueryRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.MemberDao
import java.util.UUID

@PersistenceAdapterTest
class MemberPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberMapper = MemberMapper()
		val memberRepository = MemberRepository(dslContext, memberMapper)
		val memberQueryRepository = MemberQueryRepository(dslContext, memberMapper)

		val adapter = MemberPersistenceAdapter(memberRepository)

		val memberDao = MemberDao(configuration)

		val uuid = UUID.randomUUID()
		val email = "hello@gmail.com"
		val member =
			Member(
				nickname = "nickname",
				email = Email(email),
				profileImage = "image.png",
				certificationId = uuid,
			)

		"저장을 수행한다" {
			val savedMember = adapter.insert(member)

			savedMember.id shouldNotBe null
			savedMember.roles.shouldNotBeEmpty()
			savedMember.nickname shouldBe member.nickname
			savedMember.email.address shouldBe member.email.address
			savedMember.profileImage shouldBe member.profileImage
			savedMember.certificationId shouldBe uuid
		}

		"업데이트를 수행한다" {
			val savedMember = adapter.insert(member)
			val targetMember =
				Member(
					nickname = "newNickName",
					email = Email("newEmail@gmail.com"),
					profileImage = "new.png",
					certificationId = savedMember.certificationId,
					id = savedMember.id,
				)

			adapter.update(targetMember)

			val result = memberDao.findById(targetMember.id)!!

			result.nickname shouldBe targetMember.nickname
			UUIDUtils.byteArrayToUUID(result.certificationId) shouldBe targetMember.certificationId
		}
	})