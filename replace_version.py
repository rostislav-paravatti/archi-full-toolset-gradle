import os


def replace_text_in_files(rootdir, search_text, replace_text):
    for subdir, dirs, files in os.walk(rootdir):
        for file in files:
            if file.endswith('pom.xml'):
                file_path = os.path.join(subdir, file)
                with open(file_path, 'r', encoding='utf-8') as f:
                    data = f.read().replace(search_text, replace_text)
                with open(file_path, mode='w') as f:
                    f.write(data)


#replace_text_in_files(r'/home/wsl-user/AARCHI_CO/archi-release_5.2.0', '<modelVersion>5.2.0</modelVersion>', '<modelVersion>4.0.0</modelVersion>')
replace_text_in_files(r'/home/wsl-user/AARCHI_CO/archi-release_5.2.0', '<version>${modelVersion}</version>', '<version>${revision}</version>')
