pluginManagement {
    repositories {
        // 国内镜像源（阿里云），Kotlin 语法：双引号 + 括号包裹 url
        maven(url = "https://mirrors.aliyun.com/repository/google")
        maven(url = "https://mirrors.aliyun.com/repository/jcenter")
        maven(url = "https://mirrors.aliyun.com/repository/public")
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 同样配置国内镜像源
        maven(url = "https://mirrors.aliyun.com/repository/google")
        maven(url = "https://mirrors.aliyun.com/repository/jcenter")
        maven(url = "https://mirrors.aliyun.com/repository/public")
        google()
        mavenCentral()
    }
}

rootProject.name = "PhotoEditApp"
include(":app")