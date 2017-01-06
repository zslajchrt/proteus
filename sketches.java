// the singleton
()
// another symbol for the singleton
Unit

// a plain object
def a
// another plain object
def b

// identity test
a == a // TRUE
a == b // FALSE

def c : a {}
c == a // FALSE

// x is a synonym for y
def x : y

x == y // TRUE

// x is a new fragment depending on y
def x : y {}

x == y // FALSE

// the following x are equivalent and synonymous to Unit
def x : y
def x : y ()
x : y Unit

// the following two x are equivalent
def x : {}
def x : () {}

// Z yields 4 "free" alternatives
def X : U | W
def A : B | C
def Z : X * A

// Z yields 2 "free" alternatives
def X : U | W
def A : (B : U) | (C : W)
def Z : X * A

// X yields no "free" alternative. It has 2 "bound" alternatives bound to X's state
// Y yields 2 "free" alternatives X*B and X*C of 4 total
def X : U | W {
	var b: Boolean = false
	var n: String = null
} use b, n select (b, n) { // the "from" specifies members from which the current 
						   // alternative is selected. This syntax allows monitoring of
						   // of changes on those members
	case (true, _) => U
	case (false, "OK") => W
	case _ => U
}
def A : B | C
def Z : X * A


// instantiation

def A(x: Int) => {
	def getX() = x
}

// the fragment above is a syntactic sugar for:

def A$init$ {
	x: Int
}

def A => A$init$ {
	def getX() = x
}

// a new instance
A(1)

// the code above is syntactic sugar for composition with an anonymous fragment
A {
	x: Int = 1
}


// objects, semi-objects, abstract objects (models)

// abstract objects (fragment, trait)
def C1 ()
def C2 (x::X)
def C3 (x:X)
def C4 {}
def C5 {x::X}
def C6 {x:X}

def C7 (x: X, y: Y) :
// eq. to:
def C7 (x: X) (y: Y) :

def C8 {x : X, y : Y} => x | y

def C9 (x: X, y: Y) : {
	z {i: Int} : (
		x.m1(i) + y.n2(i * 2)
	)
}

// instantiation of abstract objects
def c1 : C1 () // same as ()
def c1 : C1	// same as ()
def c2 : C2 (x1) // same as x1.alt
def c3 : C3 (x1) //// same as x1
def c4 : C4 {} // same as {}
def c5 : C5 { x : x1 }
def c5 : C5 { x1 }
def c6 : C6 { x : x1 }
def c6 : C6 { x1 }

def c7 : C7 (x, y) // same as (x, y)

def c7 : C7 (x) (y)
def c7a : C7 x
def c7b : c7a y

def c9 : C9 (x1, y2) // (x1, y2) { z {i: Int} => ... }
def v1 : c9[0] // x1
def v2 : c9[1] // y2
def b : c9.z{1}
// eq. to:
def b : c9.z(1) // the implicit conversion of (...) to {...} takes place, REALLY???
print (b)

def Int : -inf | ... | +inf

// snapshots (@)

def c : (a | b) {}
c = a
c2 : @c // c2 is the snapshot of c
c2 = b // does not compile
c2 = a // does not compile either
c2 == c // true
c = b
c2 == c // false

def d : x | y
d = x

def n : (c, @d)
n[0] = b // OK
n[1] = y // does not compile
n = (c, n[1]) // OK
n = (c, x) // does not compile

// variables
def x : Int // an alias to Int
def x0 : Int = 0 // 0 is immutable and set to 0, = selects the alternative from Int
def x1 : Int {} // {} actually allocates memory for the value (alternative) of x1
				// independently mutable objects have identity
x1 = 1 // = means to "promote", i.e. promote the alternative 1 in x1
x1 = 2

def x2 : Int {} = 10 // a preinitialised variable

def x3 : x1 // x3 is just an alias for x1
def x4 : @x1 // x4 is a snapshot of x1, x4 is immutable
def x5 : Int(x1) // x5 is a mutable va
def x6 : Int(x1) // x3

// functions

// functions do not use {}, thus there is no allocation and a function must be recalculated
// on every usage to get the state, i.e. the result

f1 (n : @Int) : Int = ( n*n )
// or
f1 (n : @Int) : Int = n*n

x => f1(i)
x[0] // i*i
x[1] // i

x = f1(i)
// eq. to
x => f(i)[0]

f2 (n : Int) => ( n*n )
i = 2
x = f2(i) 
x == 4 // true
print x // 4
i = 3
x == 9 // true
print x // 9

x => Int(0)
x = 1 // promote 1 in x
x = 2 // promote 2 in x

x2 => &x	// x2 is the current value of x

x = 1
// eq. to:
x (1)
// eq. to:

val x: Int
x (:Int) =>

x (f1 (2)) // (4) (2)
// eq. to:
x = f1(2)
x2 => f1 ( f1 (2) ) // f1 ( (4) (2) ) -> (16) ( (4) (2) ) -> (16, 4, 2)

y(Int*) =>
y =>> (f1(f1(2))) // 
// eq. to
y => y(f1(f1(2)))

def fact(0) : 1
def fact(1) : 1
def fact(n : @Int) : Int = n * fact(n - 1)
// def fact(n : @Int) : mul(n, fact(n - 1))

def mul(a: Int, b: Int) : Int = /* predefined */

def res1 : Int = mul(x, y)
def res2 : Int = mul(@x, @y)
def res3 : Int = @mul(x, y)
print res1 // a deep recalculation involving x and y
print res2 // a shallow recalculation involving the snapshots x and y
print res3 // no recalculation

// objects: 
1
"sdadas"
A
C(1) // C is an abstract object

// an abstract object
X => {
	def hhh(): Int
}

// object
XImp => X {
	def hhh(): Int = 1
}

// an abstract object
XImp2(i: Int) => X {
	def hhh(): Int = i
}
// the above is equivalent to:
XImp2 => {
	val i: Int
}
XImp2 => XImp2$ X {
	def hhh(): Int = i
}

// Y is a semi-object, it consists of both abstract and concrete objects
Y => A(1) X
// y is an object
y => Y XImp2(2)

// Semi-objects are usable via the LUB constructed from the concrete fragments only
// An analogy to static members
// The "static" members may be organised in multiple levels
A => B C (D | E) // assume that B, C and D fully abstract
A1 => A B(1) // A1 is a semi-object, B can be used as static member of 1st level
A2 => A1 C("Hello") // A2 is a semi-object, C can be used as static member of 2nd level
// A3 is an object (i.e. a concrete object)
A3 => A2 {
	case D =>
		def onD() = ...
	case E =>
		def onE() = ...
}

// instantiation from a fragment
Y(i: Int) => {
	def pow(j: Int): Int = `i^j`
}
X => Y(123) (A | B) // semi-object
x => X A(1) // instantiation

// Invoking a semi-object, an analogy to static members
X.pow(10) // returns 123^10

// generics

Array(X: Any)

// a morphing fragment

B(x: Int)
C(y: String)

// the values are evaluated LAZILY
A(x: Int, y: String) => B(x) | C(y)

// 
A(z: Boolean) => B | C {
	def getZ() = z
}

// Implicit, generated constructor fragment
A$init$ => B$init$ | C$init$ {
	z: Boolean
}

MyArg(i: Int) => A$init$ {
	z = i % 2 == 0
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
`print(b.x)

c => MyInst*C
`print(c.y)

// an independent object/singleton
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

x1.printMe() // In B 
// ??? x1 ~ C // morph x1 to C; i.e. change the default alternative in x1
x1.printMe() // In C

// syntax (object query)
// the query can be put against the free alternatives only
(x1 B).printMe() // In B
(x1 C).printMe() // In C

// x2 is Boolean A (B | C)
x2 => x1 {
	case B => true
	case C => false
}

if (x2) {
}

// morphing from within

A => B | C {

	def reshapeToB(): A B = this ~ B // ???

	def reshapeToC(): A C = this ~ C // ???
	
}

// instantiating the self type
A => B | C {
	def copy(): A
	
	case b: B =>
		def copy() = A {
			case B => x = b.x
			case C => y = "N/A"
		}
	case c: C =>
		def copy() = A {
			case B => x = 0
			case C => y = c.y
		}
}


// Let M = A | B is a morph model, A and B disjoint. Then there is no common interface. 
// But, one can create a reference suitable for a certain use case.
// Example: Jekyll | Hyde
// Example: Apple | Rocket
// Example: Egg | Larva | Cocon | Butterfly, the use case: we receive a box from
// a tropical country containing an exemplar of a butterfly we have ordered. The individual
// may find itself in one of the developmental stages. The use case is to respond 
// according to the butterfly's stage.
MinX => M {

     def execute(): Int

     case A =>
          override def methodInA() {
               // overriding methodInA defined in A
          }

          def execute(): Int = {
              // use A, the result is affected by the overridden methodInA
          }

     case B =>
          def execute(): Int = {
              // use B
          }
}

MinX.execute()

// Applications

MyApp(String* args) `java` {
	System.out.println(args);
}

//

import ServiceLauncher
import Server

MyApp(String* args) => Application {

	myServer => Server(args(0)) with LaunchedService {
	
		case Up =>
	
			`print("Server is up")
			
			// from LaunchedService
			def getStatusString(): String = "UP"

		case Down =>
			`print("Server is down")

			// from LaunchedService
			def getStatusString(): String = "DOWN"

			// from Server
			override def getDefaultURL() = "kkkkkkk"

	}

	myLauncher => Launcher with myServer

	def execute() = `myLauncher.launch()

	```
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
	```
	}
}


// References

X => Y | Z
U(x: X) => {
	
}

// Bindings

true
false
Boolean => true | false
0
1
2
...
Int => 0 | 1 | 2 ...
Even => 

// Arrays
c => Int^3 // the same as below
c => Int * Int * Int

// Submodels/supermodels

M1 => A1*A2 | A3*A4 | A5*A6
M2 => A1 | A3 | A5 | C1 // a supermodel, i.e. more generalised alternatives
M3 => A1*A2*B1 | A3*A4*B2 // a submodel, i.e. less specialised alternatives

// Invariance, Covariance, Contra-variance

M => A | B | C

x : M // invariance, i.e. x is a one-alt morph, the only alt must be from M
// 1. x can be directly accessed via LUB 

x <: M // covariance, i.e. x is morph mutating according to a sub-model of M
// 1. x can be directly accessed via LUB 
// 2. x can be merged with any sub-model of M:
x * {
	def p()

	case A =>
		def p() = 1
		
	case B =>
		def p() = 2
}

x >: M // contra-variance, i.e. x is morph mutating according to a super-model of M
// 1. x can be directly accessed via LUB 
// 2. x can be merged with any sub-model of M:
// 3  x can be morphed to any alternative of M
x * A
x * B

// Enumerations

f(X <: Int) => X {
	pow(x: X, y: Int): Int = `x^y`
}
xx => 1 * a | 4 * b | 7 * c
f(xx).pow(xx 4, 1)
f(xx).pow(xx c, 1)
f(xx).pow(xx 5, 1) // compile-time error

f(X <: A | B) {
	def mmm(x: X): Int = {
		
	}
}

// LUB members as an analogy to static members

a => {
	def hhh() = ...
}
A(p: Int) {
	def kkk() = ...
}
B(v: Int) => {
	def ppp() = ...
}

f(X <: A | B) {
	def mmm(x: X): Int = {
		X A(1)
	}
}


y >: F1 | F2
(y F1).ggg

// Bindings

Binding(X <: Any) => X
Binding$ {
	X <: Any
}

F(x: Int, y: String) // a fragment
FDict => Binding[F]

// DCI

Account(id: String) {
	var balance: Double
	val name: String
}

Source => Account {
	def transfer(target: Target, amount: Double) {
		`target.deposit(amount)
	}
}

Destination => Account {
	def deposit(amount: Double) {
		`balance += amount
	}
}

src => Source * myAccount
dst => Destination * yourAccount

`src.transfer(dst, 10)

App(sourceId: String, targetId: String) => DbLoader {
	
	src => Account * (Dict => Account$) * ConnectionParam[DbLoader$]
	
}

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

// instantiation with Java interop
MyApp(typeSystem: RTypeSystem) {
	myfun => typeSystem RFunction("myfun", `Random rnd = new Random(); rnd.nextInt()`)
}

// Examples

/// Expression Problem

Lit(i: Int)
Add(l: ?, r: ?)
Add(l, r) // the same as above

// a compact morph model
Eval => Lit i | Add ((Eval l) + (Eval r))

// a 
Eval => Int

case lit: Eval => Lit i

case add: Eval => Add ((Eval l) + (Eval r))

case str: Eval => String length()

Eval Add("asdasdsa", 1)

override case str: Eval => String (length() + 1)

override case str: Eval => print("Before") super print("After")

Eval Add("asdasdsa", 1)


Eval Mul ((Eval l) + (Eval r))

Eval Mul(Add(Lit(1), Lit(2)), Lit(3))

Eval Mul(Add(Lit(1), Lit(2)), Lit(3))

Eval Mul ((Eval Add(Lit(1), Lit(2))) + (Eval Lit(3)))
Eval Mul ((Eval Add ((Eval Lit(1)) + (Eval Lit(2)))) + (Eval Lit(3)))


/// Array

Array(size: Int) => $x {
	set(index, element) => $x.set(index, element) BoundCheck(size, index) 
	get(index) => $x.get(index) BoundCheck(size, index)
}

case GenArray => {
	set(index: Int, element) => store(index, element)
	get(index: Int) => retrieve(index)
}

inta => Array(10) Int
inta get(1)

IntLoc(size: Int) => GenLoc(size) {
	set(index, element: Int)
	get(index) => Int retrieve()
}


// intertwined data + metadata

// object

metaMeta => MetaMeta("Meta")

Meta(nm: String) => metaMeta {
}

metaX =>  {
}

X(i: int) => metaX {
}

x1 => X(1)
x2 => X(2)

x1 ~ metaX
x2 ~ metaX

// functions

Functor(T) {
	map(S)(mapper => {T.tp} * S.tp) => Functor * {S.tp}
}


// L-Systems

/// Pythagoras tree

A,B,L,R

PytTree => (A | B | L | R)^^ {
	case B => B B
	case A => B L A R A 
	default => A
}

ABCommon(length: Float) => prev: ABCommon? {
	val coord {
		
	}
}

A(length: Float) => private ABCommon(length) {
	override def draw => DrawCtx {
		next.draw();
	}
}

B(length: Float) => {
	def draw => DrawCtx {
		(dx, dy) = calcDelta(length)
		drawLine(x, y, x + dy, y + dy)
		x = x + dx
		y = y + dy
		next.draw();
	}
}


// fabric

C C C C C
C C C C C
C C C C C
C C C C C

-- horizontal
r[0] => (C U) (C D) (C U) (C D)
r[1] => (C D) (C U) (C D) (C U)
r[2] => (C U) (C D) (C U) (C D)
r[3] => (C D) (C U) (C D) (C U)

-- vertical
c[0] => (C D) (C U) (C D) (C U)
c[1] => (C U) (C D) (C U) (C D)
c[2] => (C D) (C U) (C D) (C U)
c[3] => (C U) (C D) (C U) (C D)

-- knots (k)

(k[0,0]r[0][0]c[0][0]) (r1[0] c0[1]) (r2[0] c0[2]) (r3[0] c0[3])
(k[0,1]r[0][1]c[1][0]) (r1[1] c1[1]) (r2[1] c1[2]) (r3[1] c1[3])
(k[0,2]r[0][2]c[2][0]) (r1[2] c2[1]) (r2[2] c2[2]) (r3[2] c2[3])
(k[0,3]r[0][3]c[3][0]) (r1[3] c3[1]) (r2[3] c3[2]) (r3[3] c3[3])

C
E
U => C D?
D => C U?
K => C C {
	default => K() (C() E()) (C() E())
	case k:K (c1:C E) (c2:C E) => k (c1 U() C() E()) c2 | k (c1 D() C() E()) c2 | k c1 (c2 U() C() E()) | k c1 (c2 D() C() E())
	case k:K (c1:C u:U c2:C) (c3:C E) => k (c1 u c2) (c3 D() C() E()) 
	case k:K (c1:C d:D c3:C) (c3:C E) => k (c1 d c2) (c3 U() C() E())
	case k:K (c1:C E) (c2:C u:U c3:C) => k (c1 D() C() E()) (c2 u c3)
	case k:K (c1:C E) (c2:C d:D c3:C) => k (c1 U() C() E()) (c2 d c3)
} {
	
}

// : vs ::
// or passing an arg by value or reference

a; b
ab => a | b

x1 => : ab // same as x1 => ab
x2 => :: ab // same as x1 => clone(ab)

a | b

y1(public x :: a | b)  // x is a copy
// also:
y1 => x :: a | b

y2(public x : a | b)   // x is the original
// also:
y2 => x : a | b

xx => a | b
yy1 = y1(xx) // 
xx = b
yy1 == b // false
yy2 = y2(xx) //
xx = b
yy2 == b // true

def x1 : (xx, 1)
def x2 : (@xx, 2)
print x1 // evaluate x1[0], x1[1] not evaluated
print x1 // again evaluate x1[0], x1[1] not evaluated
print x2 // no evaluation, since the elements are snapshots

// () vs. {}
// (x,y) - an ordered tuple, an index is used to access the elements
//		 - does not establish an identity
//		 - IS-A semantics
// {x=>a,y=>b} - an unordered tuple, the dot-notation is used to access the elements
//		 - establishes an identity
//		 - HAS-A semantics
() == ()
(()) == ()
() () == ()
{} != {}
//id:  0  1     2
       {} {} != {}
{{}} != {}

(a,b) (x, y, z) == (a, b, x, y, z)
(a,b, (x, y, z)) == (a, b, x, y, z)
(a,b, {(x, y, z)}) == (a, b, {(x, y, z)})
{a=>v1,b=>v2} {a=>x, b=>y, c=>z} == {a=>v1 x, b=>v2 y, c=>z}

(a,b) {x=>r, y=>s} {c, d} { x=>u, z=>v } == (a,b,c,d) {x=>r u, y=>s, z=>v }

a
x => a {}
y => a {}
x != y // true

x => a () // x => a
y => a () // y => a
x == y // true

// {} vertical composition, i.e. HAS-A relationship, . operator

{} {}
{/*id=1*/} {/*id=2*/} -> {/*id=3*/}

{{}}
{/*id=2*/ {/*id=1*/}} -> {/*id=2*/}

a => { // id = 1
	x => Int
	z => Z("asas")
	g => Float(0.0)
}

b => { // id = 2
	x => Int(0)
	y => Z
}

c => a b
c => { // id = 1
	x => Int
	z => Z("asas")
	g => Float(0.0)
} { // id = 2
	x => Int(0)
	y => Z
}
c => { // id = 3
	x => Int(0)
	z => Z("asas")
	g => Float(0.0)
}

x => { i => Int(0) }
y => { i => Int(0) }
x == y // false
x.i == y.i // true

x => { i => Int(0) {} }
y => { i => Int(0) {} }
x == y // false
x.i == y.i // false

// () horizontal composition, i.e. IS-A relationship, == operator comparing ids
a => (
	x => Int
	z => Z("asas")
	g => Float(0.0)
)

b => (
	x => Int(0)
	y => Z
)

c => a b
c => ( 
	x => Int(0)
	z => Z("asas")
	g => Float(0.0) 
	)
c => x: Int(0), z: Z("asas"), g: Float(0.0)

x => ( i: Int(0) ) // x => Int(0)
y => ( i: Int(0) ) // y => Int(0)
x == y // true

// mutable vs. immutable variables
// mutability vs. assignability

a => {}; b => {}; c => {}
v1 => a | b | c
// '<=' is the promotion operator 
v1 <= a // DOES NOT COMPILE, v1 has no identity with which the change could be associated
v1 == a // DOES NOT COMPILE

v2 => (a | b | c) {}
v2 <= a // OK
w2 => (a | b | c) {}
w2 <= a // OK
w2 eq v2 // false
w2 == v2 // true
vv2 => v2
vv2 == v2 // true

// evolution
v1 => {}
v1 =>> x2
v1 =>> y2?
v1 =>> v1 (a | b | c)
// or: v1 => v1 (a | b | c)
print(v1.model)
// {}  x2 y2? (a | b | c)

Int => 0 | 1 | 2 ... // the mutable integer (predefined)

i1 => Int {} // a mutable variable, uninitialized
i1 <= 1 // OK
i1 <= 2 // OK

i1 => Int {} <= 1 // an initialised mutable variable
i1 = 2 // OK

i2 => &i1

v {
	case i == 0 => a
	case i < 0 => b
	case i > 0 => c
}

X(i : Int) {
	case i < 3 as A =>
		def a() {}
	default as B =>
		def b() {}
}

Y => X {
	case X::A =>
		...
	case X::B =>
		...
}

i1 => Int(0)
x => X(i1)

i1 = 1

X$ => {
	i: Int
}
X => X$ {
}

