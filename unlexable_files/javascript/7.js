import Footer from "components/Footer/Footer";
import Navs from "components/Navs";
import React,{useState} from "react"
import NucleoIcons from "views/IndexSections/NucleoIcons";
import emailjs from 'emailjs-com'

import {
    Row, Col, Container
  } from 'reactstrap';
  import {
    Button,
    Card,
    CardHeader,
    CardBody,
    CardFooter,
    CardTitle, CardText,
    ListGroupItem,
    ListGroup,
    FormGroup,
    Label,
    Input,
    Form,
    UncontrolledTooltip,
  
  
  } from "reactstrap";
import Details from "views/IndexSections/Details";
import ExamplesNavbar from "components/Navbars/ExamplesNavbar";
import JavaScript from "views/IndexSections/JavaScript";
import ReactJstraining from  "../../assets/Syllabus.pdf"

export default function Training(){
    const [isOpen, setIsOpen] = useState(false);

  const toggle = () => setIsOpen(!isOpen);

  function sendEmail(e){
		e.preventDefault();

		emailjs.sendForm('service_gtymsrr', 'template_6r1s3c9', e.target, 'user_bKOaYiZidOEf3bp6V7HBM')
		  .then((result) => {
			  console.log(result.text);
		  }, (error) => {
			  console.log(error.text);
		  });
		  e.target.reset();
	}
    return(
        <>
        <ExamplesNavbar />
        <Container className="mt-5">          
      <img
        style={{width: '88%'}}
        alt="..."
        className="path"
        src={require("assets/img/path3.png").default}
      />



<Row >
  <Col xs="12" sm="6" className="mt-5" >
    <h1 className="text-primary">React JS with Redux Certification Training Course</h1>
    <h4><li className="p-1">Become Proficient in creating React JS Application </li></h4>
        <h4><li className="p-1">React JS Developer are highly paid in the industry </li></h4>
        <h4><li className="p-1">React JS with Redux Certification training helps you to become proficient in development of Client-side Application </li></h4>
        <h4><li className="p-1">Maintenance of React JS is carried out by Facebook and Instagram along with community of Developers </li></h4>

        
  </Col>
  
  
</Row>
</Container>
<Container><Button target="blank" href="https://form.jotform.com/212483596464465" color="primary">Enroll Now</Button></Container>
<br/><br/><br/><br/>
<Container>
<Container>
            <Row>
              <Col md="4">
                <hr className="line-primary" />
                <h1>
                  Upcoming Batches{" "}
                  <span className="text-primary">Limited seats available</span>
                </h1>
              </Col>
            </Row>
            <Row className="mt-5">
              <Col md="4">
                <Card className="card-coin card-plain">
                  <CardHeader>
                    <img
                      alt="..."
                      className="img-center img-fluid"
                      src={require("assets/img/online-class.png").default}
                    />
                  </CardHeader>
                  <CardBody>
                    <Row>
                      <Col className="text-center" md="12">
                        <h4 className="text-uppercase">October 16</h4>
                        <span>Weekends</span>
                        <hr className="line-warning" />
                      </Col>
                    </Row>
                    <Row>
                      <ListGroup>
                        <ListGroupItem>01:00pm - 03:00pm</ListGroupItem>
                        <ListGroupItem>4-6 weeks</ListGroupItem>
                        <ListGroupItem>Sat-Sun</ListGroupItem>
                      </ListGroup>
                    </Row>
                  </CardBody>
                  <CardFooter className="text-center">
                    <Button href="https://form.jotform.com/212483596464465" target="blank" className="btn-simple" color="warning">
                      Enroll Now
                    </Button>
                  </CardFooter>
                </Card>
              </Col>
              
              <Col md="4">
                <Card className="card-coin card-plain">
                  <CardHeader>
                    <img
                      alt="..."
                      className="img-center img-fluid"
                      src={require("assets/img/online-class.png").default}
                    />
                  </CardHeader>
                  <CardBody>
                    <Row>
                      <Col className="text-center" md="12">
                        <h4 className="text-uppercase">SEPTEMBER 13</h4>
                        <span>Weekdays</span>
                        <hr className="line-success" />
                      </Col>
                    </Row>
                    <Row>
                      <ListGroup>
                        <ListGroupItem>01:00pm - 02:00pm</ListGroupItem>
                        <ListGroupItem>4-6 weeks</ListGroupItem>
                        <ListGroupItem>Mon-Thu</ListGroupItem>
                      </ListGroup>
                    </Row>
                  </CardBody>
                  <CardFooter className="text-center">
                    <Button href="https://form.jotform.com/212483596464465" className="btn-simple" color="success">
                      Enroll Now
                    </Button>
                  </CardFooter>
                </Card>
              </Col>
              <Col md="4">
                <Card className="card-coin card-plain">
                  <CardHeader>
                    <img
                      alt="..."
                      className="img-center img-fluid"
                      src={require("assets/img/online-class.png").default}
                    />
                  </CardHeader>
                  <CardBody>
                    <Row>
                      <Col className="text-center" md="12">
                        <h4 className="text-uppercase">September 13</h4>
                        <span>Weekdays</span>
                        <hr className="line-info" />
                      </Col>
                    </Row>
                    <Row>
                      <ListGroup>
                        <ListGroupItem>08:30pm - 09:30pm</ListGroupItem>
                        <ListGroupItem>4-6 weeks</ListGroupItem>
                        <ListGroupItem>Mon-Thu</ListGroupItem>
                      </ListGroup>
                    </Row>
                  </CardBody>
                  <CardFooter className="text-center">
                    <Button href="https://form.jotform.com/212483596464465" className="btn-simple" color="info">
                      Enroll Now
                    </Button>
                  </CardFooter>
                </Card>
              </Col>
            </Row>
          </Container>
          <br/><br/><br/>
<Row>
              <Col md="4">
                <hr className="line-primary" />
                <h1>
                
                  <span className="text-primary">Course Details</span>
                </h1>
              </Col>
            </Row>
            <Navs/>
<br/><br/><br/>
            <Container className="mt-5 mb-5">
              <Row>
                <Col sm="12" md="6">
                  <h2>Languages & Tools you will learn</h2>
                  <span>Start learning web development from basics of HTML, CSS, Javascript.Master technologies like React and Bootstrap. Get in depth knowledge of Github and Deplyoment.</span>
                </Col>

                <Col sm="12" md="6">
                  <Row>
                    <Col>
                      <img  style={{height:"5rem", margin:"10px"}} src={require("assets/img/REACTLOGO.png").default} alt="..."/>
                    </Col>
                    <Col>
                      <img style={{height:"5rem", margin:"10px"}} src={require("assets/img/java.png").default} alt="..."/>
                    </Col>
                    <Col>
                      <img style={{height:"5rem", margin:"10px"}} src={require("assets/img/download.png").default} alt="..."/>
                    </Col>
                  </Row>
                  <Row>
                  <Col>
                      <img style={{height:"5rem", margin:"10px"}} src={require("assets/img/html.png").default} alt="..."/>
                    </Col>
                    <Col>
                      <img style={{height:"5rem", margin:"10px"}} src={require("assets/img/gith.png").default} alt="..."/>
                    </Col>
                    <Col>
                      <img style={{height:"5rem", margin:"10px"}} src={require("assets/img/css.png").default} alt="..."/>
                    </Col>
                  </Row>
                </Col>
              </Row>
            </Container><br /> <br />
            <Container>
              <Row>
                <Col xs="12" sm="6">

                <Card>
    <CardHeader style={{backgroundColor:"#238CF8", fontSize:"1.5rem"}}>Program Syllabus</CardHeader>
    <CardBody>
      
        <CardText>We have reverse engineered our syllabus by talking to the best companies and understanding what skills the industry needs the most right now.</CardText>
        <a href={ReactJstraining} target="blank" download><Button color="primary">Download Syllabus</Button></a>
    </CardBody>
</Card><br /> <br /><br />
                <h3><span className="text-success">Still confused! </span><br/>Drop your details & get a call back from our academic counselling expert</h3>
                <Card>
      <CardBody>
        <form onSubmit={sendEmail}>
          <FormGroup>
            <Row>
              <Col xs="12" sm="6">
            
            <Input
              type="text"
              name="name"
              id="firstName"
              placeholder="First Name"/>
            </Col>
            <Col xs="12" sm="6" className="mt-1">
            
            <Input
              type="email"
              name="email"
              id="exampleEmail"
              placeholder="Enter email"
            />
            </Col>
            </Row>
            
          
          </FormGroup>
          <Row>
              <Col xs="12" sm="6">
              
            <Input
              type="tel-number"
              name="contact"
              id="exampleEmail"
              placeholder="Contact No."/>
            </Col>
            <Col xs="12" sm="6" className="mt-1">
            
            <Input
              type="text"
              name="graduation"
              id="graduation"
              placeholder="Graduation Year"
            />
            </Col>
            </Row>
          <FormGroup check>
            <Label check>
              <Input type="checkbox" />{' '}
              Check me out
              <span className="form-check-sign">
                <span className="check"></span>
            </span>
            </Label>
          </FormGroup>
          <Button color="primary" type="submit">
            Request Callback
          </Button>
        </form>
      </CardBody>
    </Card>
    </Col>
    <Col xs="12" sm="6">
      <Details />
    </Col>

                
  </Row>
            
    </Container>         
</Container>
<div className="mt-5">
<Row>
        <Col sm="6" xs="12"  style={{ backgroundColor:"white"}}>
          <h2 style={{color:"#171941", textAlign:"center", padding:"4rem" }}>Get a real time project</h2>
          
          <Row style={{marginLeft:"0rem"}}>
            <Col xs="6">
            <img  style={{height:"4rem"}} src={require("assets/img/project-management.png").default} alt=".." />
            </Col>

            <Col xs="6" className="p-2">
            <h4 style={{color:"Black"}}>Get a real time project</h4>
            </Col>
           
          </Row>
          <Row style={{marginLeft:"0rem"}} className="p-2">
            <Col xs="6" >
            <img  style={{height:"4rem"}} src={require("assets/img/agile.png").default} alt=".." />
            </Col>

            <Col xs="6" className="p-2">
            <h4 style={{color:"Black"}}>Experience agile methodology & Work with industry experts</h4>
            </Col>
           
          </Row>
          <Row style={{display:"flex",justifyContent:"center"}}  className="p-2">
            <Col xs="6" style={{justifyContent:"center"}}>
            <img  style={{height:"4rem"}} src={require("assets/img/deployment.png").default} alt=".." />
            </Col>

            <Col xs="6" className="p-2" >
            <h4 style={{color:"Black"}}>Deploy your project to Production</h4>
            </Col>
           
          </Row>
         
        </Col>
        <Col sm="6" xs="12" >
          <h3 className="text-center m-2">Certification</h3>
          <img src={require("assets/img/certi.png").default} alt="" />
        </Col>
      </Row>
</div>
<Container>
  <JavaScript />


</Container>
<Footer />
        </>
    )
}
