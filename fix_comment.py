import os

location = './'
files_in_dir = []

for r, d, f in os.walk(location):
    for item in f:
        if '.java' in item:
            files_in_dir.append(os.path.join(r, item))

for fname in files_in_dir:
    f = open(fname, 'r', encoding="UTF8")
    f_raw = f.read()
    f_raw = f_raw.replace("/**", "/*")
    f_raw = f_raw.replace("*/", "**/")
    f.close()

    f = open(fname, 'w', encoding="UTF8")
    f.write(f_raw)
    f.flush()
    f.close()
    print("Done " + fname)


