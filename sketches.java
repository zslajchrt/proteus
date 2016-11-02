// X is a synonym for Y
X => Y
// X is a new fragment depending on Y
X => Y {}

// the following three X are equivalent and synonymous to Unit
X
X =>
X => Unit

// the following two X are equivalent
X {}
X => Unit {}

// Z yields 4 alternatives
X => U | W
A => B | C
Z => X * A

// Z yields 2 alternatives
X => U | W
A => (B => U) | (C => W)
Z => X * A

// instantiation

A => B(x: Int) | C(y: String)

// Implicit, generated constructor fragment
A$ => B {
	x: Int
} | C {
	y: String
}

MyArg(i: Int) => A$ {
	case B =>
	 x = i
	case C =>
	 y = "hello" + i
}

// singleton
MyArg1 => MyA$ {
	i = 1
}

// singleton
MyArg2 => MyA(2)

// singleton
MyInst => MyArg1 * A

b => MyInst*B
print(b.x)

c => MyInst*C
print(c.y)

x1 => A {
	def printMe()

	// this alternative will be singleton
	const case B =>
	 x = 1
	 
	 override def printMe() = print("In B: " + x);
	 
	// this alternative will be instantiated on every morphing
	case C =>
	 y = "hello: " + Time.now()

	 override def printMe() = print("In C: " + y);
}
x2 => x1.B
x3 => x1.C

x1.printMe() // In B 
x2.printMe() // In B 
x3.printMe() // In C
x2.printMe() // In B 
x1.printMe() // In B

x1 ~ C // morph x1 to C

x1.printMe() // In C 
x2.printMe() // In B 
x3.printMe() // In C
x2.printMe() // In B 
x1.printMe() // In C


// Types

RTypeSystem => RTypedValue * (SpecialValue | LangObject | Container)

SpecialValue => REmpty | RMissing | RNull | RUnboundValue

LangObject => RArgsValuesAndNames | RPromise | RFunction | REnvironment

Container => RAttributable * RAbstractContainer * AccessDim * ElemTypeDim

AccessDim => PlainIndexed | ForeignIndexed

PlainIndexed => RIndexedContainer * (1 | Vector | SpecialContainer)

ForeignIndexed => RForeignIndexedContainer

Vector => RAbstractVector * (1 | RSequence | RScalarVector)

SpecialContainer => RLanguage | RExpression | RConnection

ElemTypeDim => RIntAbstractContainer | RLogicalAbstractContainer |
 RDoubleAbstractContainer | RStringAbstractContainer | RComplexAbstractContainer |
 RRawAbstractContainer

// Type fragments

import TruffleObject

// it automatically becomes a singleton fragment since there is no state and it is completely
// implemented
RTruffleObject => TruffleObject {
    ForeignAccess getForeignAccess() {
        // default impl.
    }
}

RTypedValue => RTruffleObject {
    RType getRType();

    int getTypedValueInfo();
}

// a singleton fragment
REmpty => RTypedValue {
    RType getRType() {
        return RType.Null;
    }
}

// a singleton fragment
RMissing => RTypedValue {
    RType getRType() {
        return RType.Null;
    }
}

// not a singleton due to the state variable
RPromise => RTypedValue {
    private Object value = null;

    Object getValue() {
        return value;
    }
}

// A cacheable fragment due to the immutable name and target fields.
// If there already exists an instance for the same name and target
// that instance is returned instead of a new one.
cached RFunction(val String name, val RootCallTarget target) => RTypedValue {

    public RootCallTarget getTarget() {
        return target;
    }
}

REnvironment => RAttributable {
}

RAttributable => RTypedValue {
    RAttributes getAttributes();

    Object getAttr(String name) {
        RAttributes attr = getAttributes();
        return attr == null ? null : attr.get(name);
    }
}

RAbstractContainer {
    int getLength();

    Class<?> getElementClass();
}

RIndexedContainer {
    Object getDataAtAsObject(int index);
}

RIndexedForeignContainer {
	Object getDataAtAsObject(VirtualFrame frame, Node foreignRead, int index);
}

RAbstractVector {
    RAbstractVector copy();

    RVector<?> materialize();
}

// RSequence is relevant for double or int vector. It is achieved by the constraint
// RDoubleContainer | RIntContainer
RSequence => RDoubleContainer | RIntContainer {
    Object getStartObject();

    Object getStrideObject();
}

// Implementation
 
RVector => RAbstractVector

RIntVector => RVector * RIntContainer

RDoubleVector => RVector * RDoubleContainer

RLogicalVector => RVector * RLogicalContainer

RComplexVector => RVector * RComplexContainer

RStringVector => RVector * RStringContainer

RRawVector => RVector * RRawContainer

VectorImpl => RIntVector | RDoubleVector | RLogicalVector | RComplexVector | RStringVector | 
	RRawVector
	
RTruffleContainer => RForeignIndexedContainer
	
RIntTruffleContainer => RTruffleContainer * RIntContainer

RDoubleTruffleContainer => RTruffleContainer * RDoubleContainer

RLogicalTruffleContainer => RTruffleContainer * RLogicalContainer

RComplexTruffleContainer => RTruffleContainer * RComplexContainer

RRawTruffleContainer => RTruffleContainer * RRawContainer

TruffleContImpl => RIntTruffleContainer | RDoubleTruffleContainer | 
	RLogicalTruffleContainer | RComplexTruffleContainer | RRawTruffleContainer
	
Impl => VectorImpl | TruffleContImpl

