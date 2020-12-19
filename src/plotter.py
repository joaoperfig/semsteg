import os
import glob
import matplotlib.pyplot as plt


def readfile(file):
    f = open(file, "r")
    text = f.read()
    f.close()
    return text

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
    for file in files:
        print(file)
        content = readfile(directory+file)
        vals = content.split(",")
        for val in vals:
            if val != "":
                n = eval(val)
                result += [n]
                if n in counts:
                    counts[n] += 1
                else:
                    counts[n] = 1
    total = len(result)
    print(len(result))
    
    for n in sorted(list(counts)):
        print(n, "->",(counts[n]/total)*100,"%")
    
    plt.hist(result)
    plt.show()    
    
    
    
main()