// MIT License

import React from 'react';
import TopMenu from "../components/TopMenu";
import {Container, Card, Grid, Header} from "semantic-ui-react";
import Footer from "../components/Footer";

function Team() {
	return(
		<div>
			<TopMenu/>
			<Container text style={{marginTop: '5em', marginBottom: '1em'}}>
				<Header as='h1' style={{textAlign: 'center'}}>
					The VTC Team
				</Header>
			</Container>
			<Container style={{marginTop: '5em', marginBottom: '1em'}}>

				<Grid>
					<Grid.Row>
						<Grid.Column width={8}>
							<Card
								image='david_mimery.png'
								header='David Mimery'
								meta='Controls Lead'
								description='David is a graduate student and engineer living in Cambridge.'
							/>
						</Grid.Column>
						<Grid.Column width={8}>
							<Card
								image='jacob_miske.jpg'
								header='Jacob Miske'
								meta='CTO'
								description='Jacob is the technical officer and spends most of his time on embedded Linux controls.'
							/>
						</Grid.Column>
					</Grid.Row>

					<Grid.Row>
						<Grid.Column width={8}>
							<Card
								image='lisa_tang.png'
								header='Lisa Tang'
								meta='Cooling Lead'
								description='Lisa is the cooling lead, working to properly incorporate our FPSC engine.'
							/>
						</Grid.Column>
						<Grid.Column width={8}>
							<Card
								image='megan_mccandless.png'
								header='Megan M'
								meta='Structures Lead'
								description='Jacob is the technical officer and spends most of his time on embedded Linux controls.'
							/>
						</Grid.Column>
					</Grid.Row>
				</Grid>
				<Header as='h2'>What do you do here? </Header>
				<p>
					Contact J. Miske at jacobmiske@gmail.com
				</p>
			</Container>
			<Footer/>
		</div>
	);
}

export default Team;
