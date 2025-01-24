load("@rules_java//java:defs.bzl", "java_library", "java_test", "java_binary")

# Java library target
java_library(
    name = "icpv3_lib",
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

# Test target
java_test(
    name = "icpv3_test",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":icpv3_lib",
        "@maven//:org_springframework_boot_spring_boot_starter_test",
        "@maven//:io_temporal_temporal_testing",
        "@maven//:junit_junit",
        "@maven//:org_mockito_mockito_core",
        "@maven//:org_powermock_powermock_api_mockito2",
        "@maven//:org_powermock_powermock_module_junit4",
    ],
)

# Binary target (optional, for running the app)
java_binary(
    name = "icpv3_app",
    main_class = "com.example.icpv3.MainApplication",  # Replace with your main class
    deps = [":icpv3_lib"],
)
