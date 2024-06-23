val jooqVersion: String by rootProject
val mysqlConnectorVersion: String by rootProject

plugins {
	kotlin("jvm")
}

group = "kr.kro.dokbaro"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jooq:jooq-codegen:$jooqVersion")
	runtimeOnly("com.mysql:mysql-connector-j:$mysqlConnectorVersion")
}

tasks.test {
	useJUnitPlatform()
}
kotlin {
	jvmToolchain(17)
}