load("@rules_jvm_external//:defs.bzl", "artifact", "java_library", "java_test")

# Java library for the main application
java_library(
    name = "app",
    srcs = glob(["src/main/java/**/*.java"]),
    deps = [
        "@maven//:org_springframework_boot_spring_boot_starter_data_mongodb",
        "@maven//:org_springframework_boot_spring_boot_starter_web",
        "@maven//:io_temporal_temporal_sdk",
        "@maven//:io_grpc_grpc_api",
        "@maven//:io_grpc_grpc_protobuf",
        "@maven//:io_grpc_grpc_services",
        "@maven//:io_grpc_grpc_netty",
        "@maven//:org_slf4j_slf4j_nop",
        "@maven//:org_projectlombok_lombok",
    ],
)

# Java test rules
java_test(
    name = "app_test",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":app",
        "@maven//:org_springframework_boot_spring_boot_starter_test",
        "@maven//:junit_junit",
        "@maven//:org_mockito_mockito_core",
        "@maven//:org_powermock_powermock_api_mockito2",
        "@maven//:org_powermock_powermock_module_junit4",
    ],
)