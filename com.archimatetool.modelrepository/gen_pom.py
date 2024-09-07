import os

def parse_manifest(manifest_file):
    """Парсит MANIFEST.MF и извлекает необходимые значения."""
    manifest_data = {}

    with open(manifest_file, 'r') as file:
        for line in file:
            if line.strip() and ':' in line:
                key, value = line.split(':', 1)
                manifest_data[key.strip()] = value.strip()

    return manifest_data

def generate_pom(manifest_data, output_file):
    """Генерирует pom.xml на основе данных из MANIFEST.MF."""
    pom_template = f"""<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>{manifest_data.get('Bundle-SymbolicName', 'com.example')}</groupId>
    <artifactId>{manifest_data.get('Bundle-Name', 'my-artifact')}</artifactId>
    <version>{manifest_data.get('Bundle-Version', '1.0.0')}</version>
    <packaging>jar</packaging>
    
    <name>{manifest_data.get('Bundle-Name', 'My Project')}</name>
    <description>{manifest_data.get('Implementation-Title', 'A Maven project generated from MANIFEST.MF')}</description>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.2.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>{manifest_data.get('Main-Class', 'com.example.MainClass')}</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
"""

    with open(output_file, 'w') as file:
        file.write(pom_template)

def main():
    manifest_file = './META-INF/MANIFEST.MF'  # Укажите путь к вашему MANIFEST.MF
    output_file = 'pom.xml'  # Имя выходного POM файла
    
    # Парсим manifest и генерируем pom.xml
    manifest_data = parse_manifest(manifest_file)
    generate_pom(manifest_data, output_file)
    print(f"{output_file} создан успешно.")

if __name__ == "__main__":
    main()
