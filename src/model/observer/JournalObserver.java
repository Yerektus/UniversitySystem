package model.observer;

import model.research.Journal;
import model.research.ResearchPaper;

public interface JournalObserver {
    void onPaperPublished(ResearchPaper paper, Journal journal);
}