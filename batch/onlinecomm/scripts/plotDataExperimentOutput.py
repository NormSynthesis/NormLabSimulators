#!/usr/bin/python

import matplotlib.pyplot as plt
import sys
import os


def main(dataFile):
    values = []
    xvalues = []

    file = open(dataFile, 'r')
    lines = file.readlines()
    file.close()

    # Get legend
    legend = lines[0].split(';')[1:]
    del lines[0]

    for line in lines:
        series = []

        elements = line.split(';')
        xvalues.append(float(elements[0]))

        for i in range(1,len(elements)):
            series.append(float(elements[i]))

        values.append(series)

    # Axis
    plt.xticks(range(0, int(max(xvalues))+1, 1000), size='medium')
    plt.legend(legend, 'upper center', shadow=True, fancybox=True)
    plt.xlabel('Tick', fontsize=12)
    plt.ylabel('', fontsize=12)

    # Plot
    plt.plot(values)

    # Legend
    ax = plt.subplot(111)
    box = ax.get_position()
    ax.set_position([box.x0, box.y0, box.width, box.height * 0.9])
    plt.legend(legend, loc='upper center', ncol=2, fancybox=True, shadow=True, bbox_to_anchor=(box.width/1.6,box.height*1.55), prop={'size':11})

    # Save figure or show
    # plt.savefig('ExperimentGraphic.png')
    base = os.path.splitext(dataFile)[0]


    plt.savefig(base + ".png")

    # print "Plot successfully generated"
    # plt.show()
    print base + " plot DONE"
    # Close plot
    plt.close()

if __name__ == '__main__':
    dataFile = sys.argv[1]
    main(dataFile)
