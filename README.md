# ASTD-PO-Generation-Rodin-Plugin

## Description

This project is related to the paper "A Proof-Based Approach for ASTD Correctness" submitted in iFS 2027.
This project contains a Rodin plugin as a .jar file, the Java code of the plugin in the directory "source_plugin" and examples to test the plugin in "Examples".

## Installation

To install the plugin, please download the .jar file at the root of the project. Then put the .jar file in the directory "rodin/plugins" where Rodin is installed on your machine.

## Plugin Usage

When Rodin is open, create a new Event-B project. Then, with the Project Explorer, add the specification JSON file in the new project directory. In the Project Explorer, select the file and right-click. Then in the menu, select "ASTD Correctness POs Generation > from json ASTD to EventB Context". The Rodin context will be automatically generated.
Status on the generation will be printed in the console "CONSOLE_Plugin_ASTD_PO_Generation" during the execution.

## Examples

In examples, JSON files correspond to the specification of an ASTD. The associated PNG corresponds to the graphical representation of the specified ASTD. The associated PDF corresponds to the generated Rodin context.

## Contributors and Acknowledgement

This project has contributors and acknowledgment. They will be revealed when the paper is accepted.