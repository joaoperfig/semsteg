import os
import glob
import matplotlib.pyplot as plt


def readfile(file):
    f = open(file, "r")
    text = f.read()
    f.close()
    return text

def main(section_size):
    #directory = "../data/cohacorpus_stats/"
    #directory = "../data/globwbe_stats/"
    directory = "../data/allstats/"
    owd = os.getcwd()
    os.chdir(directory)
    files = glob.glob("*.txt")
    os.chdir(owd)
    
    sections = []
    current = []
   
    for file in files:
        print(file)
        content = readfile(directory+file)
        vals = content.split(",")
        for val in vals:
            if val != "":
                n = eval(val)
                if (len(current) < section_size):
                    current += [n]
                else:
                    sections += [current]
                    current = []
                    
    reps = []
    dreps = {}
    maxi = 512
    for section in sections:
        rep = 1
        for word in section:
            rep = rep*word
        if rep > maxi:       #avoid super long list
            rep= maxi        
        reps += [rep]

        if (rep in dreps):
            dreps[rep] += 1
        else:
            dreps[rep] = 1
    print(maxi)
    x = list(range(1, maxi+1))
    y = []
    for i in x:
        if (i in dreps):
            y += [(dreps[i]/len(sections))*100]
        else:
            y += [0]
    
    #print(reps)
    #plt.hist(reps)
    datastring = ""
    java = "{"
    for i in range(len(x)):
        datastring += "("+str(x[i])+","+str(y[i])+")"
        java += str(y[i]/100) + ","
    java = java[:-1] + "};"
        
    print (datastring)
    print (java)
    return java
    #plt.bar(x, y)
    #plt.show()        

x = list(range(20, 501, 40))
stri = ""
for i in x:
    java = main(i)
    java = "Double[] measurements"+str(i)+" = "+java
    print(i)
    stri += java + "\n"
    
stri2 = ""
for i in x:
    line = "if (reps == "+str(i)+") "+ "Collections.addAll(result, measurements"+str(i)+");"
    stri2 += line + "\n"

print(stri)
print(stri2)
