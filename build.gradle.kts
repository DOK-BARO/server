import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.Strategy

val jooqVersion: String by rootProject
val jjwtVersion: String by rootProject
val kotestVersion: String by rootProject
val kotestExtensionVersion: String by rootProject
val mokkVersion: String by rootProject
val springMockkVersion: String by rootProject
val logbackVersion: String by rootProject
val kotlinLoggingVersion: String by rootProject
val fixtureMonkeyVersion: String by rootProject

configurations {
	create("asciidoctorExt")
}

plugins {
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	id("com.google.cloud.tools.jib") version "3.4.2"
	id("org.jetbrains.kotlinx.kover") version "0.8.0"
	id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
	id("dev.monosoul.jooq-docker") version "6.0.26"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "kr.kro.dokbaro"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")
extra["springCloudVersion"] = "2023.0.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("com.mysql:mysql-connector-j")

	// monitoring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	implementation("com.github.loki4j:loki-logback-appender:$logbackVersion")
	implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")

	// jooq
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	jooqCodegen(project(":jooq-configuration"))
	jooqCodegen("org.jooq:jooq:$jooqVersion")
	jooqCodegen("org.jooq:jooq-meta:$jooqVersion")
	jooqCodegen("org.jooq:jooq-codegen:$jooqVersion")
	jooqCodegen("org.flywaydb:flyway-core:10.8.1")
	jooqCodegen("org.flywaydb:flyway-mysql:10.8.1")

	// kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// flyway
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-mysql")

	// security
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")

	// jwt
	implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

	// email
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")

	// test
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:$fixtureMonkeyVersion")

	// restdocs
	"asciidoctorExt"("org.springframework.restdocs:spring-restdocs-asciidoctor")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

	// kotest
	testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
	testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestExtensionVersion")

	// mockk
	testImplementation("io.mockk:mockk:$mokkVersion")
	testImplementation("com.ninja-squad:springmockk:$springMockkVersion")

	// test container
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mysql")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
	configurations("asciidoctorExt")
	baseDirFollowsSourceFile()
	inputs.dir(project.extra["snippetsDir"]!!)
	dependsOn(tasks.test)

	doFirst {
		delete(file("src/main/resources/static/docs"))
	}
}

tasks.register<Copy>("copyDocument") {
	dependsOn(tasks.named("asciidoctor"))
	from(file("build/docs/asciidoc"))
	into(file("src/main/resources/static/docs"))
}

tasks.build {
	dependsOn(tasks.named("copyDocument"))
}

jib {
	from {
		image = "openjdk:17-slim"
	}
	to {
		image = "phjppo0918/dokbaro-server"
		tags = mutableSetOf(project.findProperty("docker.image.tag") as String?)
		auth {
			username = project.findProperty("docker.repository.username") as String?
			password = project.findProperty("docker.repository.password") as String?
		}
	}
	container {
		creationTime = "USE_CURRENT_TIMESTAMP"
		ports = listOf("8080", "8081")
		jvmFlags = listOf("-Dspring.profiles.active=${project.findProperty("docker.image.profiles")}")
	}
}

kover {
	reports {
		filters {
			includes {
				classes("**.*")
			}
			excludes {
				classes("**.ServerApplicationKt")
				classes("org.jooq.generated.**")
				classes("kr.kro.dokbaro.server.configuration.**")
				classes("kr.kro.dokbaro.server.**.*Config")
				classes("kr.kro.dokbaro.server.common.**")
				classes("kr.kro.dokbaro.server.log.**")
				classes("kr.kro.dokbaro.server.core.security.**")
				classes("kr.kro.dokbaro.server.core.emailauthentication.adapter.out.web.SMTPEmailCodeSender")
				classes("kr.kro.dokbaro.server.core.image.**")
				classes("kr.kro.dokbaro.server.core.**.adapter.input.event.*EventListener")
				classes("kr.kro.dokbaro.server.core.**.adapter.out.persistence.entity.**.*Mapper")
				classes("kr.kro.dokbaro.server.core.**.adapter.out.persistence.repository.**.*RecordFieldName")
			}
		}
		verify {
			rule {
				disabled = false
				groupBy = GroupingEntityType.CLASS
				bound {
					minValue = 100
					coverageUnits = CoverageUnit.INSTRUCTION
					aggregationForGroup = AggregationType.COVERED_PERCENTAGE
				}
				bound {
					minValue = 100
					coverageUnits = CoverageUnit.BRANCH
					aggregationForGroup = AggregationType.COVERED_PERCENTAGE
				}
			}
		}
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

val dbUser = "root"
val dbPassword = "verysecret"
val schema = project.findProperty("mysql.schema") as String? ?: "mydatabase"

sourceSets {
	main {
		kotlin {
			srcDirs(listOf("src/main/kotlin", "src/generated"))
		}
	}
}

jooq {
	version = jooqVersion
	withContainer {
		image {
			name = "mysql:8.0.33"
			envVars =
				mapOf(
					"MYSQL_ROOT_PASSWORD" to dbPassword,
					"MYSQL_DATABASE" to schema,
				)
		}

		db {
			username = dbUser
			password = dbPassword
			name = schema
			port = 3306
			jdbc {
				schema = "jdbc:mysql"
				driverClassName = "com.mysql.cj.jdbc.Driver"
			}
		}
	}
}

tasks {
	generateJooqClasses {
		schemas.set(listOf(schema))
		outputDirectory.set(project.layout.projectDirectory.dir("src/generated"))
		includeFlywayTable.set(false)

		usingJavaConfig {
			generate =
				Generate()
					.withJavaTimeTypes(true)
					.withDeprecated(false)
					.withDaos(true)
					.withFluentSetters(true)
					.withRecords(true)

			withStrategy(
				Strategy().withName("jooq.configuration.generator.JPrefixGeneratorStrategy"),
			)

			database.withForcedTypes(
				ForcedType()
					.withTypes("int unsigned")
					.withUserType("java.lang.Long"),
				ForcedType()
					.withTypes("tinyint unsigned")
					.withUserType("java.lang.Integer"),
				ForcedType()
					.withTypes("smallint unsigned")
					.withUserType("java.lang.Integer"),
				ForcedType()
					.withIncludeTypes("TINYINT\\(1\\)")
					.withUserType("java.lang.Boolean"),
			)
		}
	}
}