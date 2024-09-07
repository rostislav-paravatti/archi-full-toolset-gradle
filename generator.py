import os
import xml.etree.ElementTree as ET

def find_files(directory, extensions):
    files = []
    for root, _, filenames in os.walk(directory):
        for filename in filenames:
            if any(filename.endswith(ext) for ext in extensions):
                files.append(os.path.join(root, filename))
    return files

def parse_pom(file):
    try:
        tree = ET.parse(file)
        root = tree.getroot()
        namespaces = {'m': 'http://maven.apache.org/POM/4.0.0'}

        group_id_elem = root.find('m:groupId', namespaces)
        if group_id_elem is None:
            group_id_elem = root.find('.//m:groupId', namespaces)
        group_id = group_id_elem.text if group_id_elem is not None else None

        artifact_id = root.find('m:artifactId', namespaces).text
        version_elem = root.find('m:version', namespaces)
        if version_elem is None:
            version_elem = root.find('.//m:version', namespaces)
        version = version_elem.text if version_elem is not None else None

        dependencies = []
        deps = root.find('m:dependencies', namespaces)
        if deps is not None:
            for dep in deps.findall('m:dependency', namespaces):
                dep_group = dep.find('m:groupId', namespaces).text
                dep_artifact = dep.find('m:artifactId', namespaces).text
                dep_version = dep.find('m:version', namespaces)
                dep_version = dep_version.text if dep_version is not None else "+"
                dependencies.append((dep_group, dep_artifact, dep_version))

        return group_id, artifact_id, version, dependencies
    except Exception as e:
        print(f"Ошибка при обработке {file}: {e}")
        return None, None, None, []

def create_settings_gradle(modules):
    with open('settings.gradle.kts', 'w') as f:
        f.write('rootProject.name = "your-project-name"\n\n')
        f.write('include(\n')
        for module in modules:
            f.write(f'    "{module}",\n')
        f.write(')\n')

def create_build_gradle(module, group, version, dependencies):
    build_gradle_path = os.path.join(module, 'build.gradle.kts')
    os.makedirs(os.path.dirname(build_gradle_path), exist_ok=True)

    with open(build_gradle_path, 'w') as f:
        f.write('plugins {\n')
        f.write('    kotlin("jvm") version "1.5.31"\n')
        f.write('    id("java")\n')
        f.write('}\n\n')
        if group:
            f.write(f'group = "{group}"\n')
        if version:
            f.write(f'version = "{version}"\n')
        f.write('\nrepositories {\n')
        f.write('    mavenCentral()\n')
        f.write('    gradlePluginPortal()\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.maven.apache.org/maven2")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://jcenter.bintray.com/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://oss.sonatype.org/content/repositories/releases/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.spring.io/release")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://jitpack.io")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://plugins.gradle.org/m2/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://maven.google.com")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.eclipse.org/content/groups/releases/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.eclipse.org/content/groups/build/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.eclipse.org/content/groups/snapshots/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.eclipse.org/content/groups/staging/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://download.eclipse.org/releases/latest/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://download.eclipse.org/egit/github/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.eclipse.org/content/repositories/egit-releases/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.eclipse.org/content/repositories/egit-snapshots/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.eclipse.org/content/repositories/egit-staging/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.eclipse.org/content/repositories/jgit-releases/")\n')
        f.write('    }\n')
        f.write('    maven {\n')
        f.write('        url = uri("https://repo.eclipse.org/content/repositories/jgit-snapshots/")\n')
        f.write('    }\n')
        f.write('}\n\n')
        if dependencies:
            f.write('dependencies {\n')
            for group, artifact, version in dependencies:
                f.write(f'    implementation("{group}:{artifact}:{version}")\n')
            f.write('}\n')

def main():
    project_dir = os.getcwd()
    modules = []

    pom_files = find_files(project_dir, ['pom.xml'])
    for pom_file in pom_files:
        group_id, artifact_id, version, dependencies = parse_pom(pom_file)
        module_path = os.path.relpath(os.path.dirname(pom_file), project_dir)
        modules.append(module_path.replace(os.sep, '.'))
        create_build_gradle(module_path, group_id, version, dependencies)

    create_settings_gradle(modules)
    print("settings.gradle.kts и build.gradle.kts успешно созданы.")

if __name__ == "__main__":
    main()
