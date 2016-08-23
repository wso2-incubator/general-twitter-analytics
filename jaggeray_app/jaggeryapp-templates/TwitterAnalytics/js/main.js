
    // Document on load.
    $(function() {

 	var newColour=["#fa574b","#3ec2ee","#e51000","#1c40fb"];
        mainCount();     
	setInterval(mainCount, 1000);
        popularOnTopic();
        popularLink();
        getNews();
        getPersonDataCloud("WCR1","G1_P1",newColour[0] );
        getPersonDataCloud("WCD1","G2_P1",newColour[1] );
        getPersonDataCloud("WCR2","G1_P2",newColour[2] );
        getPersonDataCloud("WCD2","G2_P2",newColour[3] );
     
     });
