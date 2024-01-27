import os
import sys

input = ["Expr",
         ["Binary", ["Expr", "left"], ["Expr", "right"], ["Token", "op"]],
         ["Unary", ["Expr","right"], ["Token","op"]],
         ["Grouping", ["Expr", "expr"]],
         ["Literal", ["Object", "value"]],
    ]

source = ""

source += "package jlox;\n\n"



source += "abstract class " + input[0] + "{\n\n"

# adding interface visitor
source += "\tinterface Visitor<R> {\n"

for l in input[1:]:
    class_name = l[0]
    
    #add visitor method for each class
    source += "\t\tR "
    source += "visit"+class_name+"Expr("+class_name+" expr);\n"

source += "\t}\n"

# Adding abstract accept method that each subclass will override
source +="\n\n"
source += "\tabstract <R> R accept(Visitor<R> visitor);\n\n"

for l in input[1:]:
    class_name = l[0]
    
    # add class
    source += "\tstatic class " + class_name + " extends " + input[0] + "{\n\n"
    
    # add fields
    for field in l[1:]:
        type = field[0];
        name = field[1];
        source += "\t\tfinal " + type + " " + name + ";\n";
    
    
    # add constructor
    source +="\n";
    source += "\t\t" + class_name + "( "
    
    #add formals to constructor
    for field in l[1:]:
        type = field[0];
        name = field[1];
        source += type + " " + name + ", "
    
    source = source[:-2]
    source += " ){\n\n"
    
    for field in l[1:]:
        type = field[0];
        name = field[1];
        source += "\t\t\tthis." + name + " = " + name + ";\n"
    
    source += "\n\t\t}" #closing constructor
    
    # override accept method
    source += "\n\n"
    source += "\t\t@Override\n"
    source += "\t\t<R> R accept(Visitor<R> visitor){\n"
    source += "\t\t\treturn visitor.visit"+class_name+"Expr(this);"
    source += "\n\t\t}"
    
    source += "\n\t}\n\n" #closing class
    
source += "}\n" # closing Expr

print(source)

absolute_path = os.path.dirname(__file__)
relative_path = "../jlox/AST.java"
full_path = os.path.join(absolute_path, relative_path)

with open(full_path,'w') as f:
    f.write(source)




    
    
    
    
    