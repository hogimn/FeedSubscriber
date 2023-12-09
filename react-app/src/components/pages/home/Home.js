// noinspection JSValidateTypes,JSUnresolvedReference

import React, {useEffect, useState} from 'react';
import {Button, Card, CardActions, CardContent, Grid, Typography} from '@mui/material';
import AuthorFilter from './AuthorFilter';
import RssApi from "../../api/RssApi";

function Home() {
    const [rssItems, setRssItems] = useState([]);
    const [selectedAuthors, setSelectedAuthors] = useState([]);
    const [authorBackgroundColors, setAuthorBackgroundColors] = useState({});

    useEffect(() => {
        RssApi.fetchRssItems(setRssItems)
    }, []);

    const getRandomSemiTransparentColor = () => {
        return `rgba(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(
            Math.random() * 256
        )}, 0.2)`;
    };

    const getBackgroundColorForAuthor = (author) => {
        if (!authorBackgroundColors[author]) {
            const color = getRandomSemiTransparentColor();
            setAuthorBackgroundColors((prevColors) => ({
                ...prevColors,
                [author]: color,
            }));
            return color;
        }
        return authorBackgroundColors[author];
    };

    const filteredRssFeeds =
        selectedAuthors.length > 0
            ? rssItems.filter((feed) => selectedAuthors.includes(feed.author))
            : rssItems;

    return (
        <div style={{minHeight: '100vh', padding: '16px'}}>
            <AuthorFilter
                authors={Array.from(new Set(rssItems.map((feed) => feed.author)))}
                selectedAuthors={selectedAuthors}
                setSelectedAuthors={setSelectedAuthors}
            />
            <Grid container spacing={2}>
                {filteredRssFeeds.map((feed, index) => (
                    <Grid item xs={12} sm={12} md={12} key={index}>
                        <Card>
                            <CardContent>
                                <Typography color="textSecondary">
                                    Author:
                                    <span
                                        style={{
                                            backgroundColor: getBackgroundColorForAuthor(feed.author),
                                            padding: '2px 4px',
                                            borderRadius: '4px',
                                        }}
                                    >
                                        {feed.author}
                                    </span>
                                </Typography>
                                <Typography variant="h6" gutterBottom style={{fontWeight: 'bold'}}>
                                    {feed.title}
                                </Typography>
                                <Typography
                                    color="textSecondary"
                                    gutterBottom
                                    dangerouslySetInnerHTML={{
                                        __html: feed.description.replace(
                                            /<img(.*?)src="(.*?)"(.*?)>/g,
                                            (match, p1, p2, p3) => {
                                                // Replace the unwanted part in the src attribute
                                                const newSrc = p2.replace(/=s\d+(-c-fcrop\d+=\d+,\w+)?-nd-v\d+(-rwa)?/, '');
                                                // Add style="width: 100%" to the img tag
                                                return `<img${p1}src="${newSrc}" style="width: 100%"${p3}>`;
                                            }
                                        ),
                                    }}
                                />
                                <Typography color="textSecondary" gutterBottom>
                                    Published Date: {feed.pubDate}
                                </Typography>
                            </CardContent>
                            <CardActions>
                                <Button
                                    href={feed.link}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    color="primary"
                                >
                                    Read more
                                </Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </div>
    );
}

export default Home;
