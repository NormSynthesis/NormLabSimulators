#!/usr/bin/python

__author__ = 'Iosu'

import sys
from lxml import etree
import xml.etree.ElementTree as ET

import csv
import os
import numpy as np


def main(path):#, runs):
    for i in xrange(1, 13):
        files = []
        nameXml = path+"Population" + str(i)+".xml"
        tree = ET.parse(nameXml)
        exp = etree.Element('Experiment'+str(i))
        read_population_xml(tree, exp)
        poblacion = "Population" + str(i) + "-NotConvergedNormativeSystems.dat"
        name = 'NorConvergence'
        file = path + poblacion

        if(os.path.isfile(file) == False):
            poblacion = "Population" + str(i) + "-ConvergedNormativeSystems.dat"
            file = path + poblacion
            name = 'Convergence'

        previous = ''
        ns = 1
        with open(file) as f:
            convergence = etree.Element(name)
            for line in f:
                line = line.rstrip('\n')
                if(previous == 'A'):
                    count.text = line
                    previous = ''
                if(previous == 'N'):
                    norm.text = line
                    previous = ''
                if(line is "S"):
                    system = etree.Element('NormativeSystem-'+str(ns))
                    convergence.append(system)
                    previous = 'S'
                    ns = ns + 1
                if(line is "N"):
                    norm = etree.Element('Norm')
                    system.append(norm)
                    previous = 'N'
                if(line is "A"):#Apparitions
                    count = etree.Element('apparitions')
                    system.append(count)
                    previous = 'A'

        exp.append(convergence)
        expResult.append(exp)

    # pretty string
    expResultString = etree.tostring(expResult, pretty_print=True)
    rootPretty = etree.fromstring(expResultString)
    # print s

    tree._setroot(rootPretty)
    tree.write(path+"/Averages/resume.xml", method="xml")


def read_population_xml(tree, exp):
    root = tree.getroot()
    quantity = 0
    for agent in root.iter('quantity'):
        quantity = quantity + int(agent.text)

    agentQuan = etree.Element('AgentQuantity')
    agentQuan.text = str(quantity)
    exp.append(agentQuan)

    # print agent
    population = etree.Element('Population')
    complaint = []
    for agent in root.findall('agent'):
        agents = etree.Element('Agent')
        for att in agent.iter():
            if att.tag != 'agent':
                attribute = etree.Element(''+str(att.tag))
                attribute.text = ''+str(att.text)
                agents.append(attribute)
            if att.tag == 'complaintSpam':
                complaint.append(att.text)
            # print(att.tag)
            # print att.text
        population.append(agents)
    exp.append(population)

    complaintP = etree.Element('ComplaintRate')
    complaintP.text = max(complaint)
    exp.append(complaintP)

if __name__ == '__main__':
    path = sys.argv[1]
    # runs = sys.argv[2]
    expResult = etree.Element('Results')
    main(path)#, runs)

