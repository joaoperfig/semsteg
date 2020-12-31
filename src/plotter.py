import os
import glob
import matplotlib.pyplot as plt


def readfile(file):
    f = open(file, "r")
    text = f.read()
    f.close()
    return text

def getyear(file):
    parts = file.split("_")
    for p in parts:
        if (len(p) == 4) and not ("n" in p):
            return eval(p[:3]) * 10
    return 0

def main():
    #directory = "../data/cohacorpus_stats/"
    #directory = "../data/globwbe_stats/"
    directory = "../data/allstats/"
    owd = os.getcwd()
    os.chdir(directory)
    files = glob.glob("*.txt")
    os.chdir(owd)
    result = []
    counts = {}
    doctype_c = {}
    doctype_t = {}
    years_c = {}
    years_t = {}
    for file in files:
        print(file)
        ftype = file[:2]
        year = getyear(file)
        if not (year in years_c):
            years_c[year] = 0
            years_t[year] = 0
        if not (ftype in doctype_c):
            doctype_c[ftype] = 0
            doctype_t[ftype] = 0
        content = readfile(directory+file)
        vals = content.split(",")
        for val in vals:
            if val != "":
                n = eval(val)
                result += [n]
                doctype_t[ftype] += 1
                years_t[year] += 1
                if n > 1:
                    doctype_c[ftype] += 1
                    years_c[year] += 1
                if n in counts:
                    counts[n] += 1
                else:
                    counts[n] = 1
    total = len(result)
    print(len(result))
    
    for n in sorted(list(counts)):
        print(n, "->",(counts[n]/total)*100,"%")
    
    for ftype in doctype_c:
        doctype_c[ftype] = (100*doctype_c[ftype])/doctype_t[ftype]
        
    for year in years_c:
        years_c[year] = (100*years_c[year])/years_t[year]   
        
    x = sorted(list(years_c))
    y = []
    data = ""
    for i in x:
        data += "("+str(i)+","+str(years_c[i])+")"
        y += [years_c[i]]
    
    print(doctype_c)
    print(years_c)
    print(data)
    
    plt.plot(x, y)
    #plt.hist(result)
    plt.show()    
    
    
    
main()