import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import glob
import os
import sys
import normGraph as ng

path = sys.argv[1]
pathAll = path + "*.txt"
files = glob.glob(pathAll)

for file in files:
    name = os.path.splitext(file)[0]
    os.system('python  normGraph.py '+ name +'.txt')
