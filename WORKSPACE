load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Load rules_jvm_external for managing Maven dependencies
http_archive(
    name = "rules_jvm_external",
    urls = ["https://github.com/bazelbuild/rules_jvm_external/releases/download/5.2/rules_jvm_external-5.2.tar.gz"],
    sha256 = "f3b3a11f4bdbde6c9e1b03a4f2ad6adf60b30b8d8d6e1d7465d5dbde6bde2b2b",
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

# Define Maven dependencies
maven_install(
    artifacts = [
        "org.springframework.boot:spring-boot-starter-data-mongodb:3.4.1",
        "org.springframework.boot:spring-boot-starter-web:3.4.1",
        "org.springframework.boot:spring-boot-starter-test:3.4.1",
        "io.temporal:temporal-sdk:1.24.1",
        "io.temporal:temporal-testing:1.24.1",
        "io.grpc:grpc-api:1.54.1",
        "io.grpc:grpc-protobuf:1.54.1",
        "io.grpc:grpc-services:1.54.1",
        "io.grpc:grpc-netty:1.54.1",
        "org.slf4j:slf4j-nop:2.0.6",
        "junit:junit:4.11",
        "org.mockito:mockito-core:5.10.0",
        "org.projectlombok:lombok:1.18.28",
        "org.powermock:powermock-api-mockito2:2.0.9",
        "org.powermock:powermock-module-junit4:2.0.9",
    ],
    repositories = [
        "https://repo.maven.apache.org/maven2",
    ],
)